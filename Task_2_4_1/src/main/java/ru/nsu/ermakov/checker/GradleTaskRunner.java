package ru.nsu.ermakov.checker;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Запускает Gradle задачи в проекте (с учётом наличия wrapper).
 */
public class GradleTaskRunner {
    private final CommandExecutor commandExecutor;
    private final long commandTimeoutSeconds;

    /**
     * Создаёт раннер с таймаутом по умолчанию.
     */
    public GradleTaskRunner(CommandExecutor commandExecutor) {
        this(commandExecutor, 300);
    }

    /**
     * Создаёт раннер с указанным таймаутом.
     */
    public GradleTaskRunner(CommandExecutor commandExecutor, long commandTimeoutSeconds) {
        this.commandExecutor = commandExecutor;
        this.commandTimeoutSeconds = commandTimeoutSeconds > 0 ? commandTimeoutSeconds : 300;
    }

    /**
     * Запускает компиляцию Java-кода проекта.
     */
    public ExecResult runCompile(Path projectPath) {
        return runGradle(projectPath, List.of("clean", "compileJava"));
    }

    /**
     * Запускает генерацию Javadoc.
     */
    public ExecResult runJavadoc(Path projectPath) {
        return runGradle(projectPath, List.of("javadoc"));
    }

    /**
     * Запускает проверку стиля. Пытается задачи checkstyleMain, затем fallback на checkstyle.
     */
    public ExecResult runCheckstyle(Path projectPath) {
        ExecResult mainCheck = runGradle(projectPath, List.of("checkstyleMain"));
        if (mainCheck.success() || !isMissingTaskError(mainCheck.output())) {
            return mainCheck;
        }
        return runGradle(projectPath, List.of("checkstyle"));
    }

    /**
     * Запускает тесты проекта.
     */
    public ExecResult runTests(Path projectPath) {
        return runGradle(projectPath, List.of("test"));
    }

    /**
     * Сформировать и выполнить команду gradle/gradlew с указанными задачами.
     */
    private ExecResult runGradle(Path projectPath, List<String> tasks) {
        if (projectPath == null || tasks == null || tasks.isEmpty()) {
            return ExecResult.failure("Invalid build command arguments");
        }

        List<String> command = new ArrayList<>();
        Path gradlew = projectPath.resolve("gradlew");
        Path gradlewBat = projectPath.resolve("gradlew.bat");

        boolean hasGradlew = Files.exists(gradlew);
        boolean hasGradlewBat = Files.exists(gradlewBat);
        boolean hasBuildGradle = Files.exists(projectPath.resolve("build.gradle"));
        boolean hasBuildGradleKts = Files.exists(projectPath.resolve("build.gradle.kts"));

        if (hasGradlew) {
            command.add("bash");
            command.add("./gradlew");
        } else if (hasGradlewBat) {
            command.add("cmd");
            command.add("/c");
            command.add("gradlew.bat");
        } else if (hasBuildGradle || hasBuildGradleKts) {
            command.add("gradle");
        } else {
            return ExecResult.failure("No Gradle build found in " + projectPath);
        }

        command.add("--no-daemon");
        command.add("--console=plain");
        command.addAll(tasks);

        return commandExecutor.run(projectPath, commandTimeoutSeconds, command);
    }

    /**
     * Пытается определить, что в выводе содержится сообщение о несуществующей задаче.
     */
    private boolean isMissingTaskError(String output) {
        if (output == null) {
            return false;
        }
        String lower = output.toLowerCase(Locale.ROOT);
        return lower.contains("task") && lower.contains("not found");
    }
}
