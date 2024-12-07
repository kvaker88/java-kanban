package com.yandex.app.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EpicTest {

    @Test
    void epicEqualityById() {
        Epic epic1 = new Epic(1,"Test epicEqualityById","Test epicEqualityById description");
        Epic epic12 = new Epic(2,"Test epicEqualityById","Test epicEqualityById description");

        Epic epic2 = new Epic(1,"Test epicEqualityById","Test epicEqualityById description");

        Assertions.assertEquals(epic1, epic2);
        Assertions.assertNotEquals(epic12, epic2);
    }
}
