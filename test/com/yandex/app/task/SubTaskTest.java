package com.yandex.app.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubTaskTest {

    @Test
    void epicEqualityById() {
        SubTask subTask1a = new SubTask(
                1,
                1,
                "Test epicEqualityById",
                "Test epicEqualityById description",
                Status.NEW
        );

        SubTask subTask1b = new SubTask(
                2,
                1,
                "Test epicEqualityById",
                "Test epicEqualityById description",
                Status.NEW
        );

        SubTask subTask2 = new SubTask(
                1,
                1,
                "Test epicEqualityById",
                "Test epicEqualityById description",
                Status.NEW
        );

        Assertions.assertEquals(subTask1a, subTask2);
        Assertions.assertNotEquals(subTask1b, subTask2);
    }
}
