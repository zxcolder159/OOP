package ru.nsu.ermakov.checker;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Парсер JUnit XML-отчётов для тестов сборки.
 */
public class TestReportParser {

    private static final Pattern TESTS_PATTERN = Pattern.compile("tests=\"(\\d+)\"");
    private static final Pattern FAILURES_PATTERN = Pattern.compile("failures=\"(\\d+)\"");
    private static final Pattern SKIPPED_PATTERN = Pattern.compile("skipped=\"(\\d+)\"");

    /**
     * Парсит статистику тестов из папки с результатами.
     * @param taskPath путь к директории задачи
     * @return статистика тестов
     */
    public TestStats parse(Path taskPath) {
        if (taskPath == null) {
            return TestStats.empty();
        }

        Path reportsPath = taskPath.resolve("build").resolve("test-results").resolve("test");
        if (!Files.exists(reportsPath) || !Files.isDirectory(reportsPath)) {
            return TestStats.empty();
        }

        int passed = 0;
        int failed = 0;
        int skipped = 0;

        try (Stream<Path> stream = Files.walk(reportsPath, 2)) {
            List<Path> xmlFiles = stream
                    .filter(path -> Files.isRegularFile(path) && path.toString().endsWith(".xml"))
                    .toList();

            for (Path xmlFile : xmlFiles) {
                String xml = Files.readString(xmlFile);
                int tests = matchInt(TESTS_PATTERN, xml);
                int failures = matchInt(FAILURES_PATTERN, xml);
                int skippedCount = matchInt(SKIPPED_PATTERN, xml);

                int passedCount = Math.max(0, tests - failures - skippedCount);
                passed += passedCount;
                failed += failures;
                skipped += skippedCount;
            }
        } catch (IOException e) {
            return TestStats.empty();
        }

        return new TestStats(passed, failed, skipped);
    }

    /**
     * Вспомогательный метод для извлечения целого числа по шаблону из текста.
     */
    private int matchInt(Pattern pattern, String text) {
        Matcher matcher = pattern.matcher(text);
        if (!matcher.find()) {
            return 0;
        }
        try {
            return Integer.parseInt(matcher.group(1));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
