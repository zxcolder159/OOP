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
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Точка входа приложения.
 */
public class Main {
    private static final String DEFAULT_CONFIG_FILE = "config.groovy";
    private static final String DEFAULT_REPORT_FILE = "report.html";
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
                printHtmlReport(config, repoDownloader, cliArgs.outputPath(), cliArgs.tasksFilter());
                return;
            }

            Log.info("Unknown command: %s", cliArgs.command());
            Log.info("Usage: java Main [report|clone] [config-path] [output-file] [--tasks=task1,task2]");
        } catch (IOException e) {
            Log.info("Failed to load config: %s", e.getMessage());
        }
    }

    /**
     * Генерирует и выводит HTML-отчёт.
     * @param config конфигурация
     * @param repoDownloader загрузчик репозиториев
     */
    private static void printHtmlReport(Config config, RepoDownloader repoDownloader, String outputPath, List<String> tasksFilter) {
        List<String> filter = tasksFilter == null ? List.of() : tasksFilter;
        Config filteredConfig = config;
        if (!filter.isEmpty() && config != null && config.getTasks() != null) {
            List<ru.nsu.ermakov.entity.Task> filteredTasks = config.getTasks().stream()
                .filter(task -> filter.contains(task.getId()) || filter.contains(task.getName()))
                .collect(Collectors.toList());
            filteredConfig = new ru.nsu.ermakov.entity.Config(
                config.getGroups(),
                filteredTasks,
                config.getCheckpoints(),
                config.getTaskSelections(),
                config.getSettings()
            );
        }
        CourseChecker checker = new CourseChecker(repoDownloader);
        List<StudentCheckResult> results = checker.check(filteredConfig);

        HtmlReportRenderer renderer = new HtmlReportRenderer();
        String html = renderer.render(filteredConfig, results);
        try {
            java.nio.file.Files.writeString(java.nio.file.Path.of(outputPath), html);
            Log.info("HTML-отчёт сохранён в файл: %s", outputPath);
        } catch (IOException e) {
            Log.info("Ошибка записи отчёта: %s", e.getMessage());
        }
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
            return new CliArgs(COMMAND_REPORT, DEFAULT_CONFIG_FILE, DEFAULT_REPORT_FILE, List.of());
        }

        String first = args[0];
        String configPath = args.length > 1 ? args[1] : DEFAULT_CONFIG_FILE;
        String outputPath = args.length > 2 && !args[2].startsWith("--tasks=") ? args[2] : DEFAULT_REPORT_FILE;
        List<String> tasksFilter = List.of();
        for (String arg : args) {
            if (arg != null && arg.startsWith("--tasks=")) {
                String tasksStr = arg.substring("--tasks=".length());
                tasksFilter = Arrays.stream(tasksStr.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());
            }
        }
        if (COMMAND_REPORT.equals(first) || COMMAND_CLONE.equals(first)) {
            return new CliArgs(first, configPath, outputPath, tasksFilter);
        }
        return new CliArgs(COMMAND_REPORT, first, DEFAULT_REPORT_FILE, tasksFilter);
    }

    /**
     * DTO для аргументов командной строки.
     */
    private record CliArgs(String command, String configPath, String outputPath, List<String> tasksFilter) {
        public List<String> tasksFilter() {
            return tasksFilter == null ? List.of() : tasksFilter;
        }
    }
}