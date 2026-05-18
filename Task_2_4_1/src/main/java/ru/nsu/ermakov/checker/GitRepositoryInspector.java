package ru.nsu.ermakov.checker;

import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import ru.nsu.ermakov.util.Log;

/**
 * Reads git metadata used by the checker pipeline.
 */
public class GitRepositoryInspector {
    private final CommandExecutor commandExecutor;
    private final long gitTimeoutSeconds;
    private final int semester;

    /**
     * Creates inspector with default git command timeout and auto semester detection.
     *
     * @param commandExecutor process runner
     */
    public GitRepositoryInspector(CommandExecutor commandExecutor) {
        this(commandExecutor, 60, 0);
    }

    /**
     * Creates inspector with custom git command timeout and auto semester detection.
     *
     * @param commandExecutor process runner
     * @param gitTimeoutSeconds timeout in seconds for each git call
     */
    public GitRepositoryInspector(CommandExecutor commandExecutor, long gitTimeoutSeconds) {
        this(commandExecutor, gitTimeoutSeconds, 0);
    }

    /**
     * Creates inspector with custom timeout and fixed semester for activity range.
     *
     * @param commandExecutor process runner
     * @param gitTimeoutSeconds timeout in seconds for each git call
     * @param semester 0 = auto, 1 = first (Sep–Jan), 2 = second (Feb–Jun)
     */
    public GitRepositoryInspector(CommandExecutor commandExecutor, long gitTimeoutSeconds, int semester) {
        this.commandExecutor = commandExecutor;
        this.gitTimeoutSeconds = gitTimeoutSeconds > 0 ? gitTimeoutSeconds : 60;
        this.semester = (semester == 1 || semester == 2) ? semester : 0;
    }

    /**
     * Switches repository to {@code main} or {@code master} branch.
     *
     * @param repoPath local repository path
     * @return {@code true} when checkout succeeded
     */
    public boolean checkoutMainOrMaster(Path repoPath) {
        String currentBranch = OutputUtils.firstLine(commandExecutor.run(
                repoPath,
                gitTimeoutSeconds,
                List.of(
                        "git",
                        "-C",
                        repoPath.toString(),
                        "rev-parse",
                        "--abbrev-ref",
                        "HEAD"
                )
        ).output());

        if ("main".equals(currentBranch) || "master".equals(currentBranch)) {
            return true;
        }

        ExecResult toMain = commandExecutor.run(
                repoPath,
                gitTimeoutSeconds,
                List.of("git", "-C", repoPath.toString(), "checkout", "main")
        );
        if (toMain.success()) {
            return true;
        }

        ExecResult toMaster = commandExecutor.run(
                repoPath,
                gitTimeoutSeconds,
                List.of("git", "-C", repoPath.toString(), "checkout", "master")
        );
        return toMaster.success();
    }

    /**
     * Reads the date of the last commit that touched a task directory.
     *
     * @param repoPath local repository root
     * @param taskPath path to task directory
     * @return commit date-time or {@code null} when unavailable
     */
    public LocalDateTime readLastCommitDate(Path repoPath, Path taskPath) {
        if (repoPath == null || taskPath == null) {
            Log.info("GitRepositoryInspector.readLastCommitDate: repoPath or taskPath is null (repo=%s, task=%s)", repoPath, taskPath);
            return null;
        }

        Path relativePath;
        try {
            relativePath = repoPath.relativize(taskPath);
        } catch (IllegalArgumentException e) {
            Log.info("GitRepositoryInspector.readLastCommitDate: taskPath is not inside repoPath: repo=%s, task=%s, message=%s", repoPath, taskPath, e.getMessage());
            e.printStackTrace(System.out);
            return null;
        }

        ExecResult result = commandExecutor.run(
                repoPath,
                gitTimeoutSeconds,
                List.of(
                        "git",
                        "-C",
                        repoPath.toString(),
                        "log",
                        "-1",
                        "--format=%cI",
                        "--",
                        relativePath.toString()
                )
        );

        if (!result.success() || result.output().isBlank()) {
            Log.info("GitRepositoryInspector.readLastCommitDate: git log failed or returned empty for repo=%s, path=%s, output=\n%s", repoPath, relativePath, result.output());
            return null;
        }

        try {
            return OffsetDateTime.parse(OutputUtils.firstLine(result.output())).toLocalDateTime();
        } catch (Exception e) {
            Log.info("GitRepositoryInspector.readLastCommitDate: failed to parse commit date from output, repo=%s, path=%s, message=%s", repoPath, relativePath, e.getMessage());
            e.printStackTrace(System.out);
            return null;
        }
    }

    /**
     * Collects weekly activity stats from repository commits.
     * <p>
     * When {@code semester} is 0 the range spans from the first September before the
     * latest commit up to that commit (legacy auto behaviour). When {@code semester} is
     * 1 or 2 the range is fixed to the academic semester that contains the latest commit:
     * semester 1 = Sep 1 – Jan 31, semester 2 = Feb 1 – Jun 30.
     *
     * @param repoPath local repository root
     * @return weekly activity metrics
     */
    public ActivityStats collectActivityStats(Path repoPath) {
        ExecResult result = commandExecutor.run(
                repoPath,
                gitTimeoutSeconds,
                List.of("git", "-C", repoPath.toString(), "log", "--format=%cI")
        );

        if (!result.success() || result.output().isBlank()) {
            Log.info("GitRepositoryInspector.collectActivityStats: git log failed or empty for repo=%s, output=\n%s", repoPath, result.output());
            return ActivityStats.empty();
        }

        String[] lines = result.output().split("\\R");
        List<LocalDate> dates = new ArrayList<>();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        for (String line : lines) {
            String dateText = line.trim();
            if (dateText.isEmpty()) {
                continue;
            }
            try {
                dates.add(OffsetDateTime.parse(dateText).toLocalDate());
            } catch (Exception e) {
                Log.info("GitRepositoryInspector.collectActivityStats: failed to parse date '%s' from git output for repo=%s, message=%s", dateText, repoPath, e.getMessage());
                e.printStackTrace(System.out);
            }
        }

        if (dates.isEmpty()) {
            Log.info("GitRepositoryInspector.collectActivityStats: no parsable commit dates found for repo=%s", repoPath);
            return ActivityStats.empty();
        }

        LocalDate maxDate = dates.stream().max(LocalDate::compareTo).orElse(dates.get(0));

        LocalDate rangeStart;
        LocalDate rangeEnd;

        if (semester == 1 || semester == 2) {
            LocalDate[] range = semesterRange(maxDate, semester);
            rangeStart = range[0];
            rangeEnd = range[1];
        } else {
            rangeStart = LocalDate.of(maxDate.getYear(), 9, 1);
            if (maxDate.isBefore(rangeStart)) {
                rangeStart = LocalDate.of(maxDate.getYear() - 1, 9, 1);
            }
            rangeEnd = maxDate;
        }

        Set<String> activeWeekKeys = new LinkedHashSet<>();
        for (LocalDate date : dates) {
            if (!date.isBefore(rangeStart) && !date.isAfter(rangeEnd)) {
                int week = date.get(weekFields.weekOfWeekBasedYear());
                int year = date.get(weekFields.weekBasedYear());
                activeWeekKeys.add(year + "-" + week);
            }
        }

        LocalDate effectiveEnd = (semester == 1 || semester == 2)
                ? (LocalDate.now().isBefore(rangeEnd) ? LocalDate.now() : rangeEnd)
                : rangeEnd;

        LocalDate startWeek = rangeStart.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endWeek = effectiveEnd.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        int totalWeeks = (int) ChronoUnit.WEEKS.between(startWeek, endWeek) + 1;
        if (totalWeeks <= 0) {
            totalWeeks = 1;
        }

        int activeWeeks = activeWeekKeys.size();
        double ratio = activeWeeks / (double) totalWeeks;
        return new ActivityStats(activeWeeks, totalWeeks, ratio);
    }

    /**
     * Returns [start, end] of the requested academic semester containing {@code refDate}.
     * Semester 1: Sep 1 – Jan 31 (next calendar year).
     * Semester 2: Feb 1 – Jun 30 (same calendar year as spring).
     */
    private static LocalDate[] semesterRange(LocalDate refDate, int semester) {
        int month = refDate.getMonthValue();
        int year = refDate.getYear();

        if (semester == 1) {
            int startYear = (month >= 9) ? year : year - 1;
            return new LocalDate[]{
                    LocalDate.of(startYear, 9, 8),
                    LocalDate.of(startYear, 12, 31)
            };
        } else {
            int academicStartYear = (month >= 9) ? year : year - 1;
            int springYear = academicStartYear + 1;
            return new LocalDate[]{
                    LocalDate.of(springYear, 2, 1),
                    LocalDate.of(springYear, 6, 30)
            };
        }
    }
}
