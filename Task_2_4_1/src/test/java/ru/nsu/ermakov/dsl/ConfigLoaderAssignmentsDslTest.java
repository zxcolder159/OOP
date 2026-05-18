package ru.nsu.ermakov.dsl;

import org.junit.jupiter.api.Test;
import ru.nsu.ermakov.entity.Config;
import ru.nsu.ermakov.entity.Task;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigLoaderAssignmentsDslTest {

    @Test
    void loadsAssignmentsBlock() throws Exception {
        Path tempConfig = Files.createTempFile("oop-config-", ".groovy");
        Files.writeString(tempConfig, """
                groups {
                    group {
                        name = \"24216\"
                        students = [[fio: \"Ivan Ivanov\", githubNick: \"ivan\", repoUrl: \"https://example.com/repo.git\"]]
                    }
                }

                tasks {
                    task {
                        name = \"Task_1\"
                        softDeadline = null
                        hardDeadline = null
                        maxScore = 1
                    }
                    task {
                        name = \"Task_2\"
                        softDeadline = null
                        hardDeadline = null
                        maxScore = 1
                    }
                }

                assignments {
                    assignment {
                        githubNick = \"ivan\"
                        tasks = [\"Task_2\"]
                    }
                }
                """);

        Config config = new ConfigLoader().loadConfig(tempConfig.toString());

        assertNotNull(config);
        assertEquals(1, config.getTaskSelections().size());
        assertEquals("ivan", config.getTaskSelections().get(0).getGithubNick());
        assertEquals(1, config.getTaskSelections().get(0).getTaskNames().size());
        assertEquals("Task_2", config.getTaskSelections().get(0).getTaskNames().get(0));

        Files.deleteIfExists(tempConfig);
    }

    @Test
    void supportsIncludeAndTaskIdTitle() throws Exception {
        Path dir = Files.createTempDirectory("oop-config-include-");
        Path base = dir.resolve("base.groovy");
        Path semester = dir.resolve("semester.groovy");

        Files.writeString(base, """
                tasks {
                    task {
                        id = \"Task_2_4_1\"
                        title = \"Course Checker\"
                        softDeadline = \"01-04-2026\"
                        hardDeadline = \"08-04-2026\"
                        maxScore = 5
                    }
                }
                """);

        Files.writeString(semester, """
                include \"base.groovy\"

                groups {
                    group {
                        name = \"24216\"
                        students = [[fio: \"Ivan Ivanov\", githubNick: \"ivan\", repoUrl: \"https://example.com/repo.git\"]]
                    }
                }
                """);

        Config config = new ConfigLoader().loadConfig(semester.toString());

        assertNotNull(config);
        assertEquals(1, config.getTasks().size());
        Task task = config.getTasks().get(0);
        assertEquals("Task_2_4_1", task.getId());
        assertEquals("Course Checker", task.getName());
        assertEquals((byte) 5, task.getMaxScore());
        assertEquals(1, config.getGroups().size());
        assertTrue(config.getTaskSelections().isEmpty());

        Files.deleteIfExists(semester);
        Files.deleteIfExists(base);
        Files.deleteIfExists(dir);
    }
}

