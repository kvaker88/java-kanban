package com.yandex.app.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SubTaskTest {

    @Test
    void epicEqualityById() {
        SubTask subTask1 = new SubTask(1, 1,"Test epicEqualityById",
                "Test epicEqualityById description", Status.NEW);
        SubTask subTask12 = new SubTask(2, 1,"Test epicEqualityById",
                "Test epicEqualityById description", Status.NEW);

        SubTask subTask2 = new SubTask(1, 1,"Test epicEqualityById",
                "Test epicEqualityById description", Status.NEW);

        Assertions.assertEquals(subTask1, subTask2);
        Assertions.assertNotEquals(subTask12, subTask2);
    }
}
