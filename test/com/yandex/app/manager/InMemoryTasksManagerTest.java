package com.yandex.app.manager;

import com.yandex.app.task.Epic;
import com.yandex.app.task.Status;
import com.yandex.app.task.SubTask;
import com.yandex.app.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTasksManagerTest extends TasksManagerTest<InMemoryTasksManager> {

    @BeforeEach
    public void setUp() {
        tasksManager = new InMemoryTasksManager();
    }

    @Test
    void updateEpicStatus() {
        tasksManager.addNewEpic(new Epic(
                "Test updateEpicStatus",
                "updateEpicStatus description"
        ));

        tasksManager.addNewSubtask(
                1,
                (new SubTask(1,
                        "Test#1 updateEpicStatus",
                        "Test#1 updateEpicStatus description"
                )));

        tasksManager.addNewSubtask(
                1,
                (new SubTask(
                        1,
                        "Test#2 updateEpicStatus",
                        "Test#2 updateEpicStatus description"
                )));

        assertEquals(Status.NEW, tasksManager.getEpic(1).getStatus(),
                "Ошибка при сравнивании с подзадачами в статусе NEW.");

        tasksManager.updateSubtask(new SubTask(
                2,
                1,
                "Test#1 updateEpicStatus",
                "Test#1 updateEpicStatus description",
                Status.DONE
        ));
        tasksManager.updateSubtask(new SubTask(
                3,
                1,
                "Test#1 updateEpicStatus",
                "Test#1 updateEpicStatus description",
                Status.DONE
        ));
        assertEquals(Status.DONE, tasksManager.getEpic(1).getStatus(),
                "Ошибка при сравнивании с подзадачами в статусе DONE.");

        tasksManager.updateSubtask(new SubTask(
                2,
                1,
                "Test#1 updateEpicStatus",
                "Test#1 updateEpicStatus description",
                Status.NEW
        ));
        tasksManager.updateSubtask(new SubTask(
                3,
                1,
                "Test#1 updateEpicStatus",
                "Test#1 updateEpicStatus description",
                Status.DONE
        ));
        assertEquals(Status.IN_PROGRESS, tasksManager.getEpic(1).getStatus(),
                "Ошибка при сравнивании с подзадачами в статусе NEW и DONE.");

        tasksManager.updateSubtask(new SubTask(
                2,
                1,
                "Test#1 updateEpicStatus",
                "Test#1 updateEpicStatus description",
                Status.IN_PROGRESS
        ));
        tasksManager.updateSubtask(new SubTask(
                3,
                1,
                "Test#1 updateEpicStatus",
                "Test#1 updateEpicStatus description",
                Status.IN_PROGRESS
        ));
        assertEquals(Status.IN_PROGRESS, tasksManager.getEpic(1).getStatus(),
                "Ошибка при сравнивании с подзадачами в статусе IN_PROGRESS.");
    }

    @Test
    void updateEpicDuration() {
        tasksManager.addNewEpic(new Epic(
                "Test updateEpicStatus",
                "updateEpicStatus description"
        ));

        tasksManager.addNewSubtask(
                1,
                (new SubTask(
                        2,
                        1,
                        "Test#1 updateEpicStatus",
                        "Test#1 updateEpicStatus description",
                        Status.NEW,
                        Duration.ofMinutes(15),
                        LocalDateTime.of(1970, 1, 1, 0, 0)
                )));
        tasksManager.addNewSubtask(
                1,
                (new SubTask(
                        3,
                        1,
                        "Test#1 updateEpicStatus",
                        "Test#1 updateEpicStatus description",
                        Status.NEW,
                        Duration.ofMinutes(15),
                        LocalDateTime.of(1970, 1, 1, 0, 30)
                )));

        assertEquals(Duration.ofMinutes(30), tasksManager.getEpic(1).getDuration(),
                "Длительность у эпика задана некорректно.");
    }

    @Test
    void updateEpicStartAndEndTime() {
        tasksManager.addNewEpic(new Epic(
                "Test updateEpicStartAndEndTime",
                "updateEpicStartAndEndTime description"
        ));

        tasksManager.addNewSubtask(
                1,
                (new SubTask(
                        2,
                        1,
                        "Test#1 updateEpicStartAndEndTime",
                        "Test#1 updateEpicStartAndEndTime description",
                        Status.NEW,
                        Duration.ofMinutes(30),
                        LocalDateTime.of(1970, 1, 1, 0, 0)
                )));
        tasksManager.addNewSubtask(
                1,
                (new SubTask(
                        3,
                        1,
                        "Test#1 updateEpicStartAndEndTime",
                        "Test#1 updateEpicStartAndEndTime description",
                        Status.NEW,
                        Duration.ofMinutes(20),
                        LocalDateTime.of(1970, 1, 1, 0, 30)
                )));

        assertEquals(LocalDateTime.of(1970, 1, 1, 0, 0),
                tasksManager.getEpic(1).getStartTime(),
                "Дата начала эпика задана некорректно.");
        assertEquals(LocalDateTime.of(1970, 1, 1, 0, 50),
                tasksManager.getEpic(1).getEndTime(),
                "Дата окончания эпика задана некорректно.");
    }

    @Test
    void getPrioritizedTasks() {
        Task task1 = new Task(
                1,
                "Test#1 getPrioritizedTasks",
                "Test#1 getPrioritizedTasks description",
                Status.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(1970, 1, 1, 2, 0)
        );
        Task task2 = new Task(
                2,
                "Test#2 getPrioritizedTasks",
                "Test#2 getPrioritizedTasks description",
                Status.DONE,
                Duration.ofMinutes(15),
                LocalDateTime.of(1970, 1, 1, 0, 30)
        );
        Task task3 = new Task(
                "Test#3 getPrioritizedTasks",
                "Test#3 getPrioritizedTasks description"
        );
        Task task4 = new Task(
                4,
                "Test#4 getPrioritizedTasks",
                "Test#4 getPrioritizedTasks description",
                Status.IN_PROGRESS,
                Duration.ofMinutes(15),
                LocalDateTime.of(1970, 1, 1, 1, 30)
        );

        tasksManager.addNewTask(task1);
        tasksManager.addNewTask(task2);
        tasksManager.addNewTask(task3);
        tasksManager.addNewTask(task4);

        String result = tasksManager.getPrioritizedTasks().toString();
        String correctOrder = "[" + task2 + ", " + task4 + ", " + task1 + "]";

        assertEquals(result, correctOrder, "Приоритет или кол-во задач не совпадает с ожидаемым.");
    }

    @Test
    void isOverlap() {
        Task task1 = new Task(
                1,
                "Test#1 isOverlap",
                "Test#1 isOverlap description",
                Status.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(1970, 1, 1, 0, 0)
        );
        Task task2 = new Task(
                2,
                "Test#2 isOverlap",
                "Test#2 isOverlap description",
                Status.DONE,
                Duration.ofMinutes(15),
                LocalDateTime.of(1970, 1, 1, 0, 15)
        );
        Task task3 = new Task(
                2,
                "Test#2 isOverlap",
                "Test#2 isOverlap description",
                Status.DONE,
                Duration.ofMinutes(15),
                LocalDateTime.of(1970, 1, 1, 0, 30)
        );
        Task task4 = new Task(
                2,
                "Test#2 isOverlap",
                "Test#2 isOverlap description",
                Status.DONE,
                Duration.ofMinutes(15),
                LocalDateTime.of(1970, 1, 1, 0, 40)
        );

        assertDoesNotThrow(() -> tasksManager.addNewTask(task1),
                "Добавление не пересекающихся задач привело к ошибке #1.");
        assertDoesNotThrow(() -> tasksManager.addNewTask(task2),
                "Добавление не пересекающихся задач привело к ошибке #2.");
        assertDoesNotThrow(() -> tasksManager.addNewTask(task3),
                "Добавление не пересекающихся задач привело к ошибке #3.");
        assertThrows(IllegalArgumentException.class, () -> {
            tasksManager.addNewTask(task4);
        }, "Добавление двух задач на одно время не приводит к ошибке #4.");
    }
}