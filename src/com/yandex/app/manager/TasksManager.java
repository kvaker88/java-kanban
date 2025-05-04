package com.yandex.app.manager;

import com.yandex.app.task.Epic;
import com.yandex.app.task.SubTask;
import com.yandex.app.task.Task;

import java.util.List;

public interface TasksManager {
    int addNewTask(Task task);
    int addNewEpic(Epic epic);
    int addNewSubtask(int epicId, SubTask subTask);

    List<Task> getTasks();
    List<SubTask> getSubtasks();
    List<Epic> getEpics();
    List<SubTask> getEpicSubtasks(int epicId);

    Epic getEpic(int id);
    Task getTask(int id);
    SubTask getSubtask(int id);

    void updateTask(Task task);
    void updateEpic(Epic epic);
    void updateSubtask(SubTask subTask);

    void deleteTask(int id);
    void deleteEpic(int id);
    void deleteSubtask(int id);

    void deleteTasks();
    void deleteSubtasks();
    void deleteEpics();

    List<Task> getPrioritizedTasks();
    List<Task> getHistory();
}
