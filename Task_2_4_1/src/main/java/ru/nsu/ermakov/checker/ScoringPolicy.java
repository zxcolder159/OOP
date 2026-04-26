package ru.nsu.ermakov.checker;
import ru.nsu.ermakov.entity.Checkpoint;
import ru.nsu.ermakov.entity.Config;
import ru.nsu.ermakov.entity.Status;
import ru.nsu.ermakov.entity.SystemSettings;
import ru.nsu.ermakov.entity.Task;
import java.time.LocalDateTime;

/**
 * Политика начисления баллов и конвертации их в оценки.
 */
public class ScoringPolicy {
    private final SystemSettings settings;

    /**
     * Создаёт политику с настройками по умолчанию.
     */
    public ScoringPolicy() {
        this(SystemSettings.defaults());
    }

    /**
     * Создаёт политику с указанными настройками.
     */
    public ScoringPolicy(SystemSettings settings) {
        this.settings = settings == null ? SystemSettings.defaults() : settings;
    }

    /**
     * Вычисляет балл за задачу с учётом дедлайнов и статуса.
     * @param task задача
     * @param result результат проверки задачи
     * @return целочисленный балл
     */
    public int calculateTaskScore(Task task, TaskCheckResult result) {
        if (task == null || result == null) {
            return 0;
        }

        double max = task.getMaxScore();
        if (result.getStatus() == Status.NOT_SUBMITTED) {
            return 0;
        }

        double score = max;

        score = applyDeadlinePenalty(score, task, result.getLastCommitDate());
        score = Math.max(0.0, Math.min(max, score));
        return (int) Math.round(score);
    }


    /**
     * Подсчитывает и устанавливает итоговые баллы и оценки студента.
     * @param config конфигурация
     * @param result результат проверки студента
     */
    public void applyStudentTotals(Config config, StudentCheckResult result) {
        int totalScore = 0;
        int maxScore = 0;

        for (TaskCheckResult taskResult : result.getTaskResults()) {
            totalScore += taskResult.getScore();
            if (taskResult.getTask() != null) {
                maxScore += taskResult.getTask().getMaxScore();
            }
        }

        result.setTotalScore(totalScore);
        result.setMaxScore(maxScore);

        if (config != null && config.getCheckpoints() != null) {
            for (Checkpoint checkpoint : config.getCheckpoints()) {
                if (checkpoint == null) {
                    continue;
                }
                int checkpointScore = 0;
                int checkpointMax = 0;

                for (TaskCheckResult taskResult : result.getTaskResults()) {
                    Task task = taskResult.getTask();
                    if (task == null) {
                        continue;
                    }
                    if (includedInCheckpoint(task, checkpoint)) {
                        checkpointScore += taskResult.getScore();
                        checkpointMax += task.getMaxScore();
                    }
                }

                String checkpointName;
                if (checkpoint.getName() == null) {
                    checkpointName = "checkpoint";
                } else {
                    checkpointName = checkpoint.getName();
                }
                result.getCheckpointScores().put(checkpointName, checkpointScore);
                result.getCheckpointGrades().put(checkpointName, toGrade(checkpointScore, checkpointMax));
            }
        }

        int finalGrade = toGrade(totalScore, maxScore);
        if (result.getActivityRatio() >= settings.getActivityBonusThreshold()) {
            finalGrade = Math.min(5, finalGrade + 1);
        } else {
            boolean penalize = maxScore > 0
                    && result.getActivityRatio() < settings.getActivityPenaltyThreshold();
            if (penalize) {
                finalGrade = Math.max(2, finalGrade - 1);
            }
        }
        result.setFinalGrade(finalGrade);
    }


    /**
     * Применяет штрафы за пропуск мягкого/жёсткого дедлайна.
     */
    private double applyDeadlinePenalty(double score, Task task, LocalDateTime lastCommitDate) {
        if (task == null || lastCommitDate == null) {
            return score;
        }

        LocalDateTime hardDeadline = task.getHardDeadline();
        LocalDateTime softDeadline = task.getSoftDeadline();
        int missedDeadlines = 0;

        if (softDeadline != null && lastCommitDate.isAfter(softDeadline)) {
            missedDeadlines++;
        }
        if (hardDeadline != null && lastCommitDate.isAfter(hardDeadline)) {
            missedDeadlines++;
        }

        double penalty = Math.min(
                settings.getMaxDeadlinePenalty(),
                missedDeadlines * settings.getDeadlineMissPenalty()
        );
        return score - penalty;
    }

    /**
     * Проверяет, включена ли задача в контрольную точку.
     */
    private boolean includedInCheckpoint(Task task, Checkpoint checkpoint) {
        if (checkpoint.getDate() == null || task.getHardDeadline() == null) {
            return true;
        }
        return !task.getHardDeadline().isAfter(checkpoint.getDate());
    }

    /**
     * Конвертирует сумму баллов в оценку по текущей политике.
     */
    private int toGrade(int score, int maxScore) {
        if (maxScore <= 0) {
            return 2;
        }

        double percent = (score * 100.0) / maxScore;
        if (percent >= settings.getExcellentThreshold()) {
            return 5;
        }
        if (percent >= settings.getGoodThreshold()) {
            return 4;
        }
        if (percent >= settings.getSatisfactoryThreshold()) {
            return 3;
        }
        return 2;
    }
}
