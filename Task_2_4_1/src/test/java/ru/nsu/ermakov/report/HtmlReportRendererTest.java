package ru.nsu.ermakov.report;
import org.junit.jupiter.api.Test;
import ru.nsu.ermakov.checker.StudentCheckResult;
import ru.nsu.ermakov.checker.TaskCheckResult;
import ru.nsu.ermakov.entity.Status;
import ru.nsu.ermakov.entity.Student;
import ru.nsu.ermakov.entity.Task;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class HtmlReportRendererTest {

    @Test
    void rendersBinaryIndicatorsForDocsStyleAndTests() {
        HtmlReportRenderer renderer = new HtmlReportRenderer();

        StudentCheckResult student = new StudentCheckResult("24216", new Student("Иванов И.И.", "ivanov", "repo"));
        student.markRepoPath("/tmp/repo");

        Task task = new Task("Task_1", "Task One", null, null, (byte) 10);
        TaskCheckResult taskResult = new TaskCheckResult(task);
        taskResult.setStatus(Status.PASSED);
        taskResult.setScore(10);
        taskResult.setCompilePassed(true);
        taskResult.setDocsStylePassed(false);
        taskResult.setTestsExecuted(true);
        taskResult.setTestsPassed(0);
        taskResult.setTestsFailed(0);
        taskResult.setTestsSkipped(0);
        taskResult.setLastCommitDate(LocalDateTime.of(2026, 4, 1, 12, 0));
        taskResult.setNote("OK");

        student.addTaskResult(taskResult);

        String html = renderer.render(null, List.of(student));

        assertTrue(html.contains("Docs+Style (есть/нет)"));
        assertTrue(html.contains("Тесты/покрытие (есть/нет)"));
        assertTrue(html.contains("<td>есть</td><td>нет</td><td>нет</td>"));
    }
}

