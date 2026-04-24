package ru.nsu.ermakov.checker;

import ru.nsu.ermakov.entity.Config;
import ru.nsu.ermakov.entity.Group;
import ru.nsu.ermakov.entity.Student;
import ru.nsu.ermakov.entity.SystemSettings;
import ru.nsu.ermakov.entity.Task;
import ru.nsu.ermakov.vcs.RepoDownloader;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CourseChecker {

    private final RepoDownloader repoDownloader;

    public CourseChecker(RepoDownloader repoDownloader) {
        this.repoDownloader = repoDownloader;
    }

    public List<StudentCheckResult> check(Config config) {
        if (config == null || config.getGroups() == null) {
            return Collections.emptyList();
        }

        SystemSettings settings = config.getSettings() == null ? SystemSettings.defaults() : config.getSettings();
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
            results.addAll(checkGroup(group, config, config.getTasks(), gitInspector, taskCheckService, taskSelectionResolver));
        }

        for (StudentCheckResult result : results) {
            scoringPolicy.applyStudentTotals(config, result);
        }

        return results;
    }

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
        System.err.println("\n👉 Группа: " + groupName);

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

    private StudentCheckResult checkStudent(
            String groupName,
            Student student,
            List<Task> tasks,
            GitRepositoryInspector gitInspector,
            TaskCheckService taskCheckService
    ) {
        System.err.println("   👤 Студент: " + student.getFio() + " (@" + student.getGithubNick() + ")");
        System.err.println("      🗂  Клонирование/обновление репозитория...");
        StudentCheckResult result = new StudentCheckResult(groupName, student);

        Path repoPath;
        try {
            repoPath = repoDownloader.cloneRepo(student, groupName);
            result.markRepoPath(repoPath.toString());
            System.err.println("      ✅  Репозиторий готов.");
        } catch (RuntimeException e) {
            System.err.println("      ❌  Ошибка работы с репозиторием: " + e.getMessage());
            result.markCloneError(e.getMessage());
            return result;
        }

        if (!gitInspector.checkoutMainOrMaster(repoPath)) {
            System.err.println("      ❌  Не удалось переключиться на ветку main или master");
            result.markCloneError("Unable to switch to main/master branch");
            return result;
        }

        result.applyActivity(gitInspector.collectActivityStats(repoPath));

        if (tasks == null) {
            return result;
        }

        for (Task task : tasks) {
            System.err.println("      ⚙️  Задача: " + task.getName() + "...");
            result.addTaskResult(taskCheckService.checkTask(repoPath, task));
        }

        return result;
    }

    private String sanitizeGroupName(String groupName) {
        if (groupName == null || groupName.isBlank()) {
            return "unknown-group";
        }
        return groupName;
    }
}
