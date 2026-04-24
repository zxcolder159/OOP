package ru.nsu.ermakov;

import ru.nsu.ermakov.checker.CourseChecker;
import ru.nsu.ermakov.checker.StudentCheckResult;
import ru.nsu.ermakov.checker.TaskSelectionResolver;
import ru.nsu.ermakov.dsl.ConfigLoader;
import ru.nsu.ermakov.entity.Config;
import ru.nsu.ermakov.entity.Group;
import ru.nsu.ermakov.entity.Student;
import ru.nsu.ermakov.report.HtmlReportRenderer;
import ru.nsu.ermakov.vcs.RepoDownloader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Main {
    private static final String DEFAULT_CONFIG_FILE = "config.groovy";
    private static final String COMMAND_REPORT = "report";
    private static final String COMMAND_CLONE = "clone";

    public static void main(String[] args) {
        CliArgs cliArgs = parseArgs(args);

        ConfigLoader configLoader = new ConfigLoader();
        RepoDownloader repoDownloader = new RepoDownloader();

        try {
            Config config = configLoader.loadConfig(cliArgs.configPath());
            if (COMMAND_CLONE.equals(cliArgs.command())) {
                cloneRepositories(config, repoDownloader);
                return;
            }

            if (COMMAND_REPORT.equals(cliArgs.command())) {
                printHtmlReport(config, repoDownloader);
                return;
            }

            System.err.println("Unknown command: " + cliArgs.command());
            System.err.println("Usage: java Main [report|clone] [config-path]");
        } catch (IOException e) {
            System.err.println("Failed to load config: " + e.getMessage());
        }
    }

    private static void printHtmlReport(Config config, RepoDownloader repoDownloader) {
        CourseChecker checker = new CourseChecker(repoDownloader);
        List<StudentCheckResult> results = checker.check(config);

        HtmlReportRenderer renderer = new HtmlReportRenderer();
        String html = renderer.render(config, results);
        System.out.println(html);
    }

    private static void cloneRepositories(Config config, RepoDownloader repoDownloader) {
        if (config.getGroups() == null || config.getGroups().isEmpty()) {
            System.out.println("No groups found in config.");
            return;
        }

        TaskSelectionResolver selectionResolver = new TaskSelectionResolver();

        for (Group group : config.getGroups()) {
            String groupName = group.getName() == null || group.getName().isBlank() ? "unknown-group" : group.getName();
            System.out.println("Group: " + groupName);

            if (group.getStudents() == null || group.getStudents().isEmpty()) {
                System.out.println("  No students in group.");
                continue;
            }

            for (Student student : group.getStudents()) {
                if (student == null || !selectionResolver.shouldCheckStudent(config, student)) {
                    continue;
                }
                String studentName = student != null && student.getFio() != null ? student.getFio() : "unknown-student";
                try {
                    Path repoPath = repoDownloader.cloneRepo(student, groupName);
                    System.out.println("  Cloned: " + studentName + " -> " + repoPath);
                } catch (RuntimeException e) {
                    System.err.println("  Failed: " + studentName + " -> " + e.getMessage());
                }
            }
        }
    }

    private static CliArgs parseArgs(String[] args) {
        if (args == null || args.length == 0) {
            return new CliArgs(COMMAND_REPORT, DEFAULT_CONFIG_FILE);
        }

        String first = args[0];
        if (COMMAND_REPORT.equals(first) || COMMAND_CLONE.equals(first)) {
            String configPath = args.length > 1 ? args[1] : DEFAULT_CONFIG_FILE;
            return new CliArgs(first, configPath);
        }

        return new CliArgs(COMMAND_REPORT, first);
    }

    private record CliArgs(String command, String configPath) {
    }
}