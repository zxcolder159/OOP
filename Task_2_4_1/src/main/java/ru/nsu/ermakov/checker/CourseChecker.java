package ru.nsu.ermakov.checker;
import ru.nsu.ermakov.entity.Config;
import ru.nsu.ermakov.entity.Group;
import ru.nsu.ermakov.entity.Student;
import ru.nsu.ermakov.entity.SystemSettings;
import ru.nsu.ermakov.entity.Task;
import ru.nsu.ermakov.util.Log;
import ru.nsu.ermakov.vcs.RepoDownloader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Проверяет задания курса для всех студентов и формирует результаты.
 */
public class CourseChecker {

    private final RepoDownloader repoDownloader;

    /**
     * Создаёт проверяющий с указанным загрузчиком репозиториев.
     */
    public CourseChecker(RepoDownloader repoDownloader) {
        this.repoDownloader = repoDownloader;
    }

    /**
     * Выполняет проверку по всей конфигурации курса.
     * @param config конфигурация курса
     * @return список результатов проверки студентов
     */
    public List<StudentCheckResult> check(Config config) {
        if (config == null || config.getGroups() == null) {
            return Collections.emptyList();
        }

        SystemSettings settings = config.getSettings();
        CommandExecutor commandExecutor = new CommandExecutor();
        GitRepositoryInspector gitInspector = new GitRepositoryInspector(commandExecutor, settings.getGitTimeoutSeconds());
        ScoringPolicy scoringPolicy = new ScoringPolicy(settings);
        TaskCheckService taskCheckService = new TaskCheckService(
                new TaskPathResolver(),
                gitInspector,
                new GradleTaskRunner(commandExecutor, settings.getBuildTimeoutSeconds()),
                new TestReportParser(),
                scoringPolicy
        );
        TaskSelectionResolver taskSelectionResolver = new TaskSelectionResolver();

        List<StudentCheckResult> results = new ArrayList<>();
        for (Group group : config.getGroups()) {
            List<StudentCheckResult> groupResults = checkGroup(
                    group,
                    config,
                    config.getTasks(),
                    gitInspector,
                    taskCheckService,
                    taskSelectionResolver
            );
            results.addAll(groupResults);
        }

        for (StudentCheckResult result : results) {
            scoringPolicy.applyStudentTotals(config, result);
        }

        return results;
    }

    /**
     * Проверяет одну группу студентов.
     */
    private List<StudentCheckResult> checkGroup(
            Group group,
            Config config,
            List<Task> tasks,
            GitRepositoryInspector gitInspector,
            TaskCheckService taskCheckService,
            TaskSelectionResolver taskSelectionResolver
    ) {
        if (group == null || group.getStudents() == null) {
            return Collections.emptyList();
        }

        String groupName = sanitizeGroupName(group.getName());
        Log.info("\n👉 Группа: %s", groupName);

        List<StudentCheckResult> results = new ArrayList<>();

        for (Student student : group.getStudents()) {
            if (student == null) {
                continue;
            }
            if (!taskSelectionResolver.shouldCheckStudent(config, student)) {
                continue;
            }
            List<Task> selectedTasks = taskSelectionResolver.resolveTasks(config, student, tasks);
            results.add(checkStudent(groupName, student, selectedTasks, gitInspector, taskCheckService));
        }

        return results;
    }

    /**
     * Проверяет одного студента: клонирует репозиторий, собирает и запускает тесты.
     */
    private StudentCheckResult checkStudent(
            String groupName,
            Student student,
            List<Task> tasks,
            GitRepositoryInspector gitInspector,
            TaskCheckService taskCheckService
    ) {
        Log.info("   👤 Студент: %s (@%s)", student.getFio(), student.getGithubNick());
        Log.info("      🗂  Клонирование/обновление репозитория...");
        StudentCheckResult result = new StudentCheckResult(groupName, student);

        Path repoPath;
        try {
            repoPath = repoDownloader.cloneRepo(student, groupName);
            result.markRepoPath(repoPath.toString());
            Log.info("      ✅  Репозиторий готов.");
        } catch (RuntimeException e) {
            Log.info("      ❌  Ошибка работы с репозиторием: %s", e.getMessage());
            result.markCloneError(e.getMessage());
            return result;
        }

        if (!gitInspector.checkoutMainOrMaster(repoPath)) {
            Log.info("      ❌  Не удалось переключиться на ветку main или master");
            result.markCloneError("Unable to switch to main/master branch");
            return result;
        }

        result.applyActivity(gitInspector.collectActivityStats(repoPath));

        if (tasks == null) {
            return result;
        }

        for (Task task : tasks) {
            Log.info("      ⚙️  Задача: %s...", task.getName());
            result.addTaskResult(taskCheckService.checkTask(repoPath, task));
        }

        return result;
    }

    /**
     * Нормализует имя группы для вывода/логирования.
     */
    private String sanitizeGroupName(String groupName) {
        if (groupName == null || groupName.isBlank()) {
            return "unknown-group";
        }
        return groupName;
    }
}
