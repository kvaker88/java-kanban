package com.yandex.app;

import com.yandex.app.task.Task;
import com.yandex.app.task.Epic;
import com.yandex.app.task.SubTask;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class DefaultTasksTest {

    public boolean equalsTask(Task task1, Task task2) {
        return task1.getId() == task2.getId() &&
                Objects.equals(task1.getName(), task2.getName()) &&
                Objects.equals(task1.getDescription(), task2.getDescription()) &&
                Objects.equals(task1.getStatus(), task2.getStatus()) &&
                Objects.equals(task1.getType(), task2.getType()) &&
                Objects.equals(task1.getDuration(), task2.getDuration()) &&
                Objects.equals(task1.getStartTime(), task2.getStartTime());
    }

    public boolean equalsEpic(Epic epic1, Epic epic2) {
        List<SubTask> epicSubTasks1 = epic1.getSubtasks();
        List<SubTask> epicSubTasks2 = epic2.getSubtasks();

        boolean resultEqualsSubTasks;

        if (epicSubTasks1.size() != epicSubTasks2.size()) {
            return false;
        }

        resultEqualsSubTasks = IntStream.range(0, epicSubTasks1.size())
                .allMatch(i -> equalsSubTask(epicSubTasks1.get(i), epicSubTasks2.get(i)));

        return resultEqualsSubTasks && epic1.getId() == epic2.getId() &&
                Objects.equals(epic1.getName(), epic2.getName()) &&
                Objects.equals(epic1.getDescription(), epic2.getDescription()) &&
                Objects.equals(epic1.getStatus(), epic2.getStatus()) &&
                Objects.equals(epic1.getType(), epic2.getType()) &&
                Objects.equals(epic1.getDuration(), epic2.getDuration()) &&
                Objects.equals(epic1.getStartTime(), epic2.getStartTime());
    }

    public boolean equalsSubTask(SubTask subTask1, SubTask subTask2) {
        return subTask1.getId() == subTask2.getId() &&
                subTask1.getEpicId() == subTask2.getEpicId() &&
                Objects.equals(subTask1.getName(), subTask2.getName()) &&
                Objects.equals(subTask1.getDescription(), subTask2.getDescription()) &&
                Objects.equals(subTask1.getStatus(), subTask2.getStatus()) &&
                Objects.equals(subTask1.getType(), subTask2.getType()) &&
                Objects.equals(subTask1.getDuration(), subTask2.getDuration()) &&
                Objects.equals(subTask1.getStartTime(), subTask2.getStartTime());
    }
}
