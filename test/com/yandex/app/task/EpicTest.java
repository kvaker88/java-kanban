package com.yandex.app.task;

import com.yandex.app.DefaultTasksTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EpicTest {
    DefaultTasksTest defaultTasksTest = new DefaultTasksTest();

    @Test
    void epicEqualityById() {
        Epic epic1 = new Epic(
                1,
                "Тестовый эпик #1 epicEqualityById()",
                "Описание для тестового эпика #1 epicEqualityById()"
        );

        Epic epic2 = new Epic(
                1,
                "Тестовый эпик #1 epicEqualityById()",
                "Описание для тестового эпика #1 epicEqualityById()"
        );

        Epic epic3 = new Epic(
                2,
                "Тестовый эпик #2 epicEqualityById()",
                "Описание для тестового эпика #2 epicEqualityById()"
        );

        assertTrue(defaultTasksTest.equalsEpic(epic1, epic2),
                "Идентичные эпики отличаются при сравнивании в epicEqualityById()");
        assertFalse(defaultTasksTest.equalsEpic(epic2, epic3),
                "Отличные эпики совпадают при сравнивании в epicEqualityById()");
    }
}
