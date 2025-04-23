package com.yandex.app.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    void taskEqualityById() {
        Task task1 = new Task(
                1,
                "Test taskEqualityById",
                "Test taskEqualityById description"
        );

        Task task12 = new Task(
                2,
                "Test taskEqualityById",
                "Test taskEqualityById description"
        );

        Task task2 = new Task(
                1,
                "Test taskEqualityById",
                "Test taskEqualityById description"
        );

        Assertions.assertEquals(task1, task2);
        Assertions.assertNotEquals(task12, task2);
    }
}
