package com.yandex.app.task;

import com.yandex.app.DefaultTasksTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubTaskTest {
    DefaultTasksTest defaultTasksTest = new DefaultTasksTest();

    @Test
    void subtaskEqualityById() {
        SubTask subTask1 = new SubTask(
                1,
                1,
                "Тестовая подзадача #1 subtaskEqualityById()",
                "Описание тестовой подзадачи #1 subtaskEqualityById()",
                Status.NEW
        );

        SubTask subTask2 = new SubTask(
                1,
                1,
                "Тестовая подзадача #1 subtaskEqualityById()",
                "Описание тестовой подзадачи #1 subtaskEqualityById()",
                Status.NEW
        );

        SubTask subTask3 = new SubTask(
                2,
                1,
                "Тестовая подзадача #2 subtaskEqualityById()",
                "Описание тестовой подзадачи #2 subtaskEqualityById()",
                Status.NEW
        );

        assertTrue(defaultTasksTest.equalsSubTask(subTask1, subTask2),
                "Идентичные подзадачи отличаются при сравнивании в epicEqualityById()");
        assertFalse(defaultTasksTest.equalsSubTask(subTask2, subTask3),
                "Отличные подзадачи совпадают при сравнивании в epicEqualityById()");
    }
}
