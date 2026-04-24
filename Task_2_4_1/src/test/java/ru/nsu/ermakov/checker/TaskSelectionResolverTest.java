package ru.nsu.ermakov.checker;

import org.junit.jupiter.api.Test;
import ru.nsu.ermakov.entity.Checkpoint;
import ru.nsu.ermakov.entity.Config;
import ru.nsu.ermakov.entity.Group;
import ru.nsu.ermakov.entity.Student;
import ru.nsu.ermakov.entity.StudentTaskSelection;
import ru.nsu.ermakov.entity.SystemSettings;
import ru.nsu.ermakov.entity.Task;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskSelectionResolverTest {

    @Test
    void noSelectionsFallsBackToAllStudentsAndAllTasks() {
        Student student = new Student("Ivan Ivanov", "ivan", "https://example.com/repo.git");
        List<Task> tasks = sampleTasks();
        Config config = new Config(List.of(new Group("g", List.of(student))), tasks, List.of(), List.of(), SystemSettings.defaults());

        TaskSelectionResolver resolver = new TaskSelectionResolver();

        assertTrue(resolver.shouldCheckStudent(config, student));
        assertEquals(tasks, resolver.resolveTasks(config, student, tasks));
    }

    @Test
    void selectionsLimitStudentsAndTasks() {
        Student selectedStudent = new Student("Ivan Ivanov", "ivan", "https://example.com/repo1.git");
        Student skippedStudent = new Student("Petr Petrov", "petr", "https://example.com/repo2.git");
        List<Task> tasks = sampleTasks();

        StudentTaskSelection selection = new StudentTaskSelection("ivan", null, List.of("Task_1", "Task_3"));
        Config config = new Config(
                List.of(new Group("g", List.of(selectedStudent, skippedStudent))),
                tasks,
                List.of(new Checkpoint("cp", LocalDateTime.now())),
                List.of(selection),
                SystemSettings.defaults()
        );

        TaskSelectionResolver resolver = new TaskSelectionResolver();

        assertTrue(resolver.shouldCheckStudent(config, selectedStudent));
        assertFalse(resolver.shouldCheckStudent(config, skippedStudent));
        assertEquals(List.of(tasks.get(0), tasks.get(2)), resolver.resolveTasks(config, selectedStudent, tasks));
        assertTrue(resolver.resolveTasks(config, skippedStudent, tasks).isEmpty());
    }

    @Test
    void selectionsCanReferenceTaskId() {
        Student student = new Student("Ivan Ivanov", "ivan", "https://example.com/repo1.git");
        List<Task> tasks = List.of(
                new Task("Task_2_4_1", "Course Checker", null, null, (byte) 1),
                new Task("Task_2_3_1", "Snakes", null, null, (byte) 1)
        );

        StudentTaskSelection selection = new StudentTaskSelection("ivan", null, List.of("Task_2_4_1"));
        Config config = new Config(
                List.of(new Group("g", List.of(student))),
                tasks,
                List.of(),
                List.of(selection),
                SystemSettings.defaults()
        );

        TaskSelectionResolver resolver = new TaskSelectionResolver();
        List<Task> selected = resolver.resolveTasks(config, student, tasks);

        assertEquals(1, selected.size());
        assertEquals("Course Checker", selected.get(0).getName());
    }

    private List<Task> sampleTasks() {
        return List.of(
                new Task("Task_1", null, null, (byte) 1),
                new Task("Task_2", null, null, (byte) 1),
                new Task("Task_3", null, null, (byte) 1)
        );
    }
}

