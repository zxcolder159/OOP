package ru.nsu.ermakov.checker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

public class TaskPathResolver {

    private static final int SEARCH_DEPTH = 5;

    public Path findTaskDirectory(Path repoPath, String taskName) {
        if (repoPath == null || taskName == null || taskName.isBlank()) {
            return null;
        }

        Path directPath = repoPath.resolve(taskName);
        if (Files.isDirectory(directPath)) {
            return directPath;
        }

        try (Stream<Path> stream = Files.walk(repoPath, SEARCH_DEPTH)) {
            return stream
                    .filter(Files::isDirectory)
                    .filter(path -> !path.toString().contains("/.git"))
                    .filter(path -> taskName.equalsIgnoreCase(path.getFileName().toString()))
                    .min(Comparator.comparingInt(Path::getNameCount))
                    .orElse(null);
        } catch (IOException e) {
            return null;
        }
    }
}
