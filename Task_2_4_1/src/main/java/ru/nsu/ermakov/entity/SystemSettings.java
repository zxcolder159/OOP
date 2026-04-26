package ru.nsu.ermakov.entity;

import lombok.Getter;

/**
 * Системные настройки для процесса проверки: таймауты, веса частей и пороги оценок.
 */
@Getter
public class SystemSettings {

    private static final long DEFAULT_BUILD_TIMEOUT_SECONDS = 300;
    private static final long DEFAULT_GIT_TIMEOUT_SECONDS = 60;
    private static final double DEFAULT_COMPILE_PART = 1.00;
    private static final double DEFAULT_DOCS_STYLE_PART = 0.00;
    private static final double DEFAULT_TESTS_PART = 0.00;
    private static final double DEFAULT_DEADLINE_MISS_PENALTY = 0.50;
    private static final double DEFAULT_MAX_DEADLINE_PENALTY = 1.00;
    private static final double DEFAULT_EXCELLENT_THRESHOLD = 85.0;
    private static final double DEFAULT_GOOD_THRESHOLD = 70.0;
    private static final double DEFAULT_SATISFACTORY_THRESHOLD = 50.0;
    private static final double DEFAULT_ACTIVITY_BONUS_THRESHOLD = 0.60;
    private static final double DEFAULT_ACTIVITY_PENALTY_THRESHOLD = 0.30;

    private final long buildTimeoutSeconds;
    private final long gitTimeoutSeconds;
    private final double compilePart;
    private final double docsStylePart;
    private final double testsPart;
    private final double deadlineMissPenalty;
    private final double maxDeadlinePenalty;
    private final double excellentThreshold;
    private final double goodThreshold;
    private final double satisfactoryThreshold;
    private final double activityBonusThreshold;
    private final double activityPenaltyThreshold;

    public SystemSettings(
            long buildTimeoutSeconds,
            long gitTimeoutSeconds,
            double compilePart,
            double docsStylePart,
            double testsPart,
            double deadlineMissPenalty,
            double maxDeadlinePenalty,
            double excellentThreshold,
            double goodThreshold,
            double satisfactoryThreshold,
            double activityBonusThreshold,
            double activityPenaltyThreshold
    ) {
        if (buildTimeoutSeconds > 0) {
            this.buildTimeoutSeconds = buildTimeoutSeconds;
        } else {
            this.buildTimeoutSeconds = DEFAULT_BUILD_TIMEOUT_SECONDS;
        }

        if (gitTimeoutSeconds > 0) {
            this.gitTimeoutSeconds = gitTimeoutSeconds;
        } else {
            this.gitTimeoutSeconds = DEFAULT_GIT_TIMEOUT_SECONDS;
        }

        double normalizedCompile = compilePart >= 0 ? compilePart : DEFAULT_COMPILE_PART;
        double normalizedDocs = docsStylePart >= 0 ? docsStylePart : DEFAULT_DOCS_STYLE_PART;
        double normalizedTests = testsPart >= 0 ? testsPart : DEFAULT_TESTS_PART;
        double sum = normalizedCompile + normalizedDocs + normalizedTests;

        if (sum <= 0.0) {
            this.compilePart = DEFAULT_COMPILE_PART;
            this.docsStylePart = DEFAULT_DOCS_STYLE_PART;
            this.testsPart = DEFAULT_TESTS_PART;
        } else {
            this.compilePart = normalizedCompile / sum;
            this.docsStylePart = normalizedDocs / sum;
            this.testsPart = normalizedTests / sum;
        }

        this.deadlineMissPenalty = clamp(
                deadlineMissPenalty,
                0.0,
                1000.0,
                DEFAULT_DEADLINE_MISS_PENALTY
        );
        this.maxDeadlinePenalty = clamp(
                maxDeadlinePenalty,
                0.0,
                1000.0,
                DEFAULT_MAX_DEADLINE_PENALTY
        );
        this.excellentThreshold = clamp(
                excellentThreshold,
                0.0,
                100.0,
                DEFAULT_EXCELLENT_THRESHOLD
        );
        this.goodThreshold = clamp(
                goodThreshold,
                0.0,
                100.0,
                DEFAULT_GOOD_THRESHOLD
        );
        this.satisfactoryThreshold = clamp(
                satisfactoryThreshold,
                0.0,
                100.0,
                DEFAULT_SATISFACTORY_THRESHOLD
        );
        this.activityBonusThreshold = clamp(
                activityBonusThreshold,
                0.0,
                1.0,
                DEFAULT_ACTIVITY_BONUS_THRESHOLD
        );
        this.activityPenaltyThreshold = clamp(
                activityPenaltyThreshold,
                0.0,
                1.0,
                DEFAULT_ACTIVITY_PENALTY_THRESHOLD
        );
    }

    /**
     * Возвращает настройки по умолчанию.
     */
    public static SystemSettings defaults() {
        return new SystemSettings(
                DEFAULT_BUILD_TIMEOUT_SECONDS,
                DEFAULT_GIT_TIMEOUT_SECONDS,
                DEFAULT_COMPILE_PART,
                DEFAULT_DOCS_STYLE_PART,
                DEFAULT_TESTS_PART,
                DEFAULT_DEADLINE_MISS_PENALTY,
                DEFAULT_MAX_DEADLINE_PENALTY,
                DEFAULT_EXCELLENT_THRESHOLD,
                DEFAULT_GOOD_THRESHOLD,
                DEFAULT_SATISFACTORY_THRESHOLD,
                DEFAULT_ACTIVITY_BONUS_THRESHOLD,
                DEFAULT_ACTIVITY_PENALTY_THRESHOLD
        );
    }

    /**
     * Ограничивает значение заданным диапазоном, возвращая fallback при некорректных входных данных.
     */
    private static double clamp(double value, double min, double max, double fallback) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return fallback;
        }
        if (value < min || value > max) {
            return fallback;
        }
        return value;
    }
}
