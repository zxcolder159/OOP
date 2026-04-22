package ru.nsu.ermakov.vcs;

import ru.nsu.ermakov.entity.Student;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class RepoDownloader {

	private static final long PULL_TIMEOUT_SECONDS = 120;
	private static final long CLONE_TIMEOUT_SECONDS = 240;

	public Path cloneRepo(Student student, String groupName) {
		if (student == null || student.getRepoUrl() == null || student.getRepoUrl().isBlank()) {
			throw new IllegalArgumentException("Invalid student or repoUrl");
		}

		String repoUrl = student.getRepoUrl();
		String fio = student.getFio();
		String safeName = sanitize(fio);
		String safeGroup = sanitize(groupName);

		Path projectRoot = Path.of(System.getProperty("user.dir"));
		Path destinationPath = projectRoot.resolve("repos").resolve(safeGroup).resolve(safeName);
		Path logPath = destinationPath.resolve("git.log");

		try {
			Files.createDirectories(destinationPath.getParent());

			if (Files.exists(destinationPath)) {
				Path gitDir = destinationPath.resolve(".git");
				if (Files.exists(gitDir)) {
					ProcessBuilder pbPull = new ProcessBuilder("git", "-C", destinationPath.toString(), "pull");
					pbPull.redirectErrorStream(true);
					pbPull.redirectOutput(logPath.toFile());
					Process pPull = pbPull.start();
					boolean finishedPull = pPull.waitFor(PULL_TIMEOUT_SECONDS, TimeUnit.SECONDS);
					if (!finishedPull) {
						pPull.destroyForcibly();
						throw new RuntimeException("Pull timed out for " + fio);
					}
					if (pPull.exitValue() == 0) {
						return destinationPath;
					}
				}
				Path backup = destinationPath.getParent().resolve(safeName + "_backup_" + Instant.now().toEpochMilli());
				Files.move(destinationPath, backup);
			}

			ProcessBuilder pbClone = new ProcessBuilder("git", "clone", "--depth", "1", repoUrl, destinationPath.toString());
			pbClone.redirectErrorStream(true);
			Files.createDirectories(destinationPath.getParent());
			pbClone.redirectOutput(logPath.toFile());
			Process pClone = pbClone.start();
			boolean finishedClone = pClone.waitFor(CLONE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
			if (!finishedClone) {
				pClone.destroyForcibly();
				throw new RuntimeException("Clone timed out for " + repoUrl);
			}
			if (pClone.exitValue() != 0) {
				throw new RuntimeException("Clone failed for " + repoUrl + " (exit " + pClone.exitValue() + ")");
			}

			return destinationPath;
		} catch (IOException e) {
			throw new RuntimeException("IO error for " + fio + ": " + e.getMessage(), e);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("Interrupted for " + fio, e);
		}
	}

	private static String sanitize(String s) {
		if (s == null || s.isBlank()) return "unknown";
		String sanitized = s.trim().replaceAll("[^A-Za-z0-9._-]", "_");
		if (sanitized.isBlank()) return "unknown";
		if (sanitized.length() > 80) return sanitized.substring(0, 80);
		return sanitized;
	}
}
