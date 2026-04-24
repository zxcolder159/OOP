package ru.nsu.ermakov.checker;

import org.junit.jupiter.api.Test;
import ru.nsu.ermakov.entity.Config;
import ru.nsu.ermakov.entity.SystemSettings;
import ru.nsu.ermakov.entity.Task;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoringPolicyTest {

    @Test
    void givesFullScoreWhenNoDeadlineMissedEvenIfChecksFailed() {
        SystemSettings settings = new SystemSettings(
                300,
                60,
                0.5,
                0.2,
                0.3,
                0.0,
                0.0,
                85.0,
                70.0,
                50.0,
                0.60,
                0.30
        );
        ScoringPolicy scoringPolicy = new ScoringPolicy(settings);

        Task task = new Task("Task_1", "Task One", null, null, (byte) 10);
        TaskCheckResult result = new TaskCheckResult(task);
        result.markSubmitted("Task_1", LocalDateTime.of(2026, 3, 30, 0, 0));
        result.setCompilePassed(false);
        result.setDocsStylePassed(false);
        result.setTestsExecuted(false);
        result.setTestsPassed(0);
        result.setTestsFailed(0);
        result.setTestsSkipped(0);

        int score = scoringPolicy.calculateTaskScore(task, result);

        assertEquals(10, score);
    }

    @Test
    void appliesDeadlinePenaltyFromMaxScore() {
        SystemSettings settings = new SystemSettings(
                300,
                60,
                1.0,
                0.0,
                0.0,
                0.5,
                1.0,
                85.0,
                70.0,
                50.0,
                0.60,
                0.30
        );
        ScoringPolicy scoringPolicy = new ScoringPolicy(settings);

        LocalDateTime soft = LocalDateTime.of(2026, 4, 1, 0, 0);
        LocalDateTime hard = LocalDateTime.of(2026, 4, 8, 0, 0);
        Task task = new Task("Task_1", "Task One", soft, hard, (byte) 5);

        TaskCheckResult result = new TaskCheckResult(task);
        result.markSubmitted("Task_1", LocalDateTime.of(2026, 4, 10, 0, 0));
        result.setCompilePassed(true);

        int score = scoringPolicy.calculateTaskScore(task, result);

        assertEquals(4, score);
    }

    @Test
    void activityAffectsFinalGrade() {
        ScoringPolicy scoringPolicy = new ScoringPolicy(SystemSettings.defaults());

        Task task = new Task("Task_1", null, null, (byte) 10);
        TaskCheckResult taskResult = new TaskCheckResult(task);
        taskResult.setScore(10);

        StudentCheckResult student = new StudentCheckResult("g", null);
        student.addTaskResult(taskResult);
        student.applyActivity(new ActivityStats(1, 10, 0.1));

        Config config = new Config(List.of(), List.of(task), List.of(), List.of(), SystemSettings.defaults());
        scoringPolicy.applyStudentTotals(config, student);

        assertEquals(10, student.getTotalScore());
        assertEquals(10, student.getMaxScore());
        assertEquals(4, student.getFinalGrade());
    }
}
