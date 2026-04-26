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

    /**
     * Creates inspector with default git command timeout.
     *
     * @param commandExecutor process runner
     */
    public GitRepositoryInspector(CommandExecutor commandExecutor) {
        this(commandExecutor, 60);
    }

    /**
     * Creates inspector with custom git command timeout.
     *
     * @param commandExecutor process runner
     * @param gitTimeoutSeconds timeout in seconds for each git call
     */
    public GitRepositoryInspector(CommandExecutor commandExecutor, long gitTimeoutSeconds) {
        this.commandExecutor = commandExecutor;
        this.gitTimeoutSeconds = gitTimeoutSeconds > 0 ? gitTimeoutSeconds : 60;
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
        Set<String> weeks = new LinkedHashSet<>();
        List<LocalDate> dates = new ArrayList<>();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        for (String line : lines) {
            String dateText = line.trim();
            if (dateText.isEmpty()) {
                continue;
            }
            try {
                LocalDate date = OffsetDateTime.parse(dateText).toLocalDate();
                dates.add(date);
                int week = date.get(weekFields.weekOfWeekBasedYear());
                int year = date.get(weekFields.weekBasedYear());
                weeks.add(year + "-" + week);
            } catch (Exception e) {
                Log.info("GitRepositoryInspector.collectActivityStats: failed to parse date '%s' from git output for repo=%s, message=%s", dateText, repoPath, e.getMessage());
                e.printStackTrace(System.out);
            }
        }

        if (dates.isEmpty()) {
            Log.info("GitRepositoryInspector.collectActivityStats: no parsable commit dates found for repo=%s", repoPath);
            return ActivityStats.empty();
        }

        LocalDate minDate = dates.stream().min(LocalDate::compareTo).orElse(dates.get(0));
        LocalDate maxDate = dates.stream().max(LocalDate::compareTo).orElse(dates.get(0));

        LocalDate minWeekStart = minDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate maxWeekStart = maxDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        int totalWeeks = (int) ChronoUnit.WEEKS.between(minWeekStart, maxWeekStart) + 1;
        if (totalWeeks <= 0) {
            totalWeeks = 1;
        }

        int activeWeeks = weeks.size();
        double ratio = activeWeeks / (double) totalWeeks;
        return new ActivityStats(activeWeeks, totalWeeks, ratio);
    }
}
