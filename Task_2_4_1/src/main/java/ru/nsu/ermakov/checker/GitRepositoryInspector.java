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

public class GitRepositoryInspector {
    private final CommandExecutor commandExecutor;
    private final long gitTimeoutSeconds;

    public GitRepositoryInspector(CommandExecutor commandExecutor) {
        this(commandExecutor, 60);
    }

    public GitRepositoryInspector(CommandExecutor commandExecutor, long gitTimeoutSeconds) {
        this.commandExecutor = commandExecutor;
        this.gitTimeoutSeconds = gitTimeoutSeconds > 0 ? gitTimeoutSeconds : 60;
    }

    public boolean checkoutMainOrMaster(Path repoPath) {
        String currentBranch = OutputUtils.firstLine(commandExecutor.run(
                repoPath,
                gitTimeoutSeconds,
                List.of("git", "-C", repoPath.toString(), "rev-parse", "--abbrev-ref", "HEAD")
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

    public LocalDateTime readLastCommitDate(Path repoPath, Path taskPath) {
        if (repoPath == null || taskPath == null) {
            return null;
        }

        Path relativePath;
        try {
            relativePath = repoPath.relativize(taskPath);
        } catch (IllegalArgumentException e) {
            return null;
        }

        ExecResult result = commandExecutor.run(
                repoPath,
                gitTimeoutSeconds,
                List.of("git", "-C", repoPath.toString(), "log", "-1", "--format=%cI", "--", relativePath.toString())
        );

        if (!result.success() || result.output().isBlank()) {
            return null;
        }

        try {
            return OffsetDateTime.parse(OutputUtils.firstLine(result.output())).toLocalDateTime();
        } catch (Exception e) {
            return null;
        }
    }

    public ActivityStats collectActivityStats(Path repoPath) {
        ExecResult result = commandExecutor.run(
                repoPath,
                gitTimeoutSeconds,
                List.of("git", "-C", repoPath.toString(), "log", "--format=%cI")
        );

        if (!result.success() || result.output().isBlank()) {
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
            } catch (Exception ignored) {
                // Ignore invalid git dates
            }
        }

        if (dates.isEmpty()) {
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
