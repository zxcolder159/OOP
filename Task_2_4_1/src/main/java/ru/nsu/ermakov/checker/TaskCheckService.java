package ru.nsu.ermakov.checker;

import ru.nsu.ermakov.entity.Task;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TaskCheckService {

    private static final int NOTE_LIMIT = 300;

    private final TaskPathResolver taskPathResolver;
    private final GitRepositoryInspector gitInspector;
    private final GradleTaskRunner gradleTaskRunner;
    private final TestReportParser testReportParser;
    private final ScoringPolicy scoringPolicy;

    public TaskCheckService(
            TaskPathResolver taskPathResolver,
            GitRepositoryInspector gitInspector,
            GradleTaskRunner gradleTaskRunner,
            TestReportParser testReportParser,
            ScoringPolicy scoringPolicy
    ) {
        this.taskPathResolver = taskPathResolver;
        this.gitInspector = gitInspector;
        this.gradleTaskRunner = gradleTaskRunner;
        this.testReportParser = testReportParser;
        this.scoringPolicy = scoringPolicy;
    }

    public TaskCheckResult checkTask(Path repoPath, Task task) {
        TaskCheckResult result = new TaskCheckResult(task);

        if (task == null || task.getName() == null || task.getName().isBlank()) {
            result.markNotSubmitted("Invalid task in config");
            return result;
        }

        Path taskPath = taskPathResolver.findTaskDirectory(repoPath, task.getName());
        if (taskPath == null) {
            result.markNotSubmitted("Task folder not found in repository");
            return result;
        }

        result.markSubmitted(taskPath.toString(), gitInspector.readLastCommitDate(repoPath, taskPath));

        ExecResult compileResult = gradleTaskRunner.runCompile(taskPath);
        result.setCompilePassed(compileResult.success());
        if (!compileResult.success()) {
            result.markFailed(OutputUtils.shorten(compileResult.output(), NOTE_LIMIT));
            result.setScore(scoringPolicy.calculateTaskScore(task, result));
            return result;
        }

        ExecResult javadocResult = gradleTaskRunner.runJavadoc(taskPath);
        ExecResult styleResult = gradleTaskRunner.runCheckstyle(taskPath);
        result.setDocsStylePassed(javadocResult.success() && styleResult.success());
        List<String> warnings = new ArrayList<>();
        if (!result.isDocsStylePassed()) {
            warnings.add("Замечание по javadoc/checkstyle: "
                    + OutputUtils.shorten(javadocResult.output() + "\n" + styleResult.output(), NOTE_LIMIT));
        }

        if (result.isDocsStylePassed()) {
            ExecResult testResult = gradleTaskRunner.runTests(taskPath);
            TestStats testStats = testReportParser.parse(taskPath);

            result.setTestsExecuted(testResult.success() || testStats.total() > 0);
            result.setTestsPassed(testStats.passed());
            result.setTestsFailed(testStats.failed());
            result.setTestsSkipped(testStats.skipped());

            if (!testResult.success()) {
                warnings.add("Тесты завершились с ошибкой запуска: " + OutputUtils.shorten(testResult.output(), NOTE_LIMIT));
            } else if (result.getTestsFailed() > 0) {
                warnings.add("Есть упавшие тесты: " + result.getTestsFailed());
            }
        } else {
            result.setTestsExecuted(false);
            result.setTestsPassed(0);
            result.setTestsFailed(0);
            result.setTestsSkipped(0);
            warnings.add("Тесты не запускались: этап docs/style завершился с ошибкой");
        }

        String note = warnings.isEmpty() ? "OK" : String.join(" | ", warnings);
        result.markPassed(note);

        result.setScore(scoringPolicy.calculateTaskScore(task, result));
        return result;
    }
}
