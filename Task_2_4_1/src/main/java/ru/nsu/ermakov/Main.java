package ru.nsu.ermakov;
import ru.nsu.ermakov.checker.CourseChecker;
import ru.nsu.ermakov.util.Log;
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

/**
 * Точка входа приложения.
 */
public class Main {
    private static final String DEFAULT_CONFIG_FILE = "config.groovy";
    private static final String COMMAND_REPORT = "report";
    private static final String COMMAND_CLONE = "clone";

    /**
     * Главный метод приложения.
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        CliArgs cliArgs = parseArgs(args);

        ConfigLoader configLoader = new ConfigLoader();
        RepoDownloader repoDownloader = new RepoDownloader();

        try {
            Config config = configLoader.loadConfig(cliArgs.configPath());
            if (COMMAND_CLONE.equals(cliArgs.command())) {
                new Main().cloneRepositories(config, repoDownloader);
                return;
            }

            if (COMMAND_REPORT.equals(cliArgs.command())) {
                printHtmlReport(config, repoDownloader);
                return;
            }

            Log.info("Unknown command: %s", cliArgs.command());
            Log.info("Usage: java Main [report|clone] [config-path]");
        } catch (IOException e) {
            Log.info("Failed to load config: %s", e.getMessage());
        }
    }

    /**
     * Генерирует и выводит HTML-отчёт.
     * @param config конфигурация
     * @param repoDownloader загрузчик репозиториев
     */
    private static void printHtmlReport(Config config, RepoDownloader repoDownloader) {
        CourseChecker checker = new CourseChecker(repoDownloader);
        List<StudentCheckResult> results = checker.check(config);

        HtmlReportRenderer renderer = new HtmlReportRenderer();
        String html = renderer.render(config, results);
        System.out.println(html);
    }

    /**
     * Клонирует репозитории студентов.
     * @param config конфигурация
     * @param repoDownloader загрузчик репозиториев
     */
    private void cloneRepositories(Config config, RepoDownloader repoDownloader) {
        if (config.getGroups() == null || config.getGroups().isEmpty()) {
            System.out.println("No groups found in config.");
            return;
        }

        TaskSelectionResolver selectionResolver = new TaskSelectionResolver();

        for (Group group : config.getGroups()) {
            String groupName;
            if (group.getName() == null || group.getName().isBlank()) {
                groupName = "unknown-group";
            } else {
                groupName = group.getName();
            }
            System.out.println("Group: " + groupName);

            if (group.getStudents() == null || group.getStudents().isEmpty()) {
                System.out.println("  No students in group.");
                continue;
            }

            for (Student student : group.getStudents()) {
                if (student == null || !selectionResolver.shouldCheckStudent(config, student)) {
                    continue;
                }
                String studentName = "unknown-student";
                if (student != null && student.getFio() != null) {
                    studentName = student.getFio();
                }
                try {
                    Path repoPath = repoDownloader.cloneRepo(student, groupName);
                    Log.info("  Cloned: %s -> %s", studentName, repoPath);
                } catch (RuntimeException e) {
                    Log.info("  Failed: %s -> %s", studentName, e.getMessage());
                }
            }
        }
    }

    /**
     * Парсит аргументы командной строки.
     * @param args аргументы
     * @return объект с командой и путём к конфигу
     */
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

    /**
     * DTO для аргументов командной строки.
     */
    private record CliArgs(String command, String configPath) {
    }
}