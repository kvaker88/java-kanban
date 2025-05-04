package com.yandex.app.task;

import com.yandex.app.DefaultTasksTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskTest {
    DefaultTasksTest defaultTasksTest = new DefaultTasksTest();

    @Test
    void taskEqualityById() {
        Task task1 = new Task(
                1,
                "Тестовая задача #1 taskEqualityById()",
                "Описание для тестовой задачи #1 taskEqualityById()"
        );

        Task task2 = new Task(
                1,
                "Тестовая задача #1 taskEqualityById()",
                "Описание для тестовой задачи #1 taskEqualityById()"
        );

        Task task3 = new Task(
                2,
                "Тестовая задача #2 taskEqualityById()",
                "Описание для тестовой задачи #2 taskEqualityById()"
        );

        assertTrue(defaultTasksTest.equalsTask(task1, task2),
                "Идентичные задачи отличаются при сравнивании в taskEqualityById()");
        assertFalse(defaultTasksTest.equalsTask(task2, task3),
                "Отличные задачи совпадают при сравнивании в taskEqualityById()");
    }
}
