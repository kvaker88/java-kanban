package com.yandex.app.manager;

import com.yandex.app.DefaultTasksTest;
import com.yandex.app.task.Epic;
import com.yandex.app.task.Status;
import com.yandex.app.task.SubTask;
import com.yandex.app.task.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TasksManagerTest<T extends TasksManager> {
    DefaultTasksTest defaultTasksTest = new DefaultTasksTest();
    protected T tasksManager;

    @Test
    void addNewTask() {
        Task task = new Task("Тестовая задача addNewTask()",
                "Описание тестовой задачи addNewTask()",
                null,
                null);

        final int taskId = tasksManager.addNewTask(task);
        final Task savedTask = tasksManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена в addNewTask()");
        assertTrue(defaultTasksTest.equalsTask(task, savedTask),
                "Задачи отличаются при сравнивании в addNewTask()");

        final List<Task> tasks = tasksManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются в addNewTask()");
        assertEquals(1, tasks.size(), "Неверное количество задач в addNewTask()");
        assertTrue(defaultTasksTest.equalsTask(task, tasks.getFirst()),
                "Задачи отличаются при сравнивании в addNewTask()");
    }

    @Test
    void addNewEpic() {
        Epic epic = new Epic("Тестовый эпик addNewEpic()",
                "Описание тестового эпика addNewEpic()",
                null,
                null);

        final int taskId = tasksManager.addNewEpic(epic);
        final Epic savedEpic = tasksManager.getEpic(taskId);

        assertNotNull(savedEpic, "Эпик не найдена в addNewEpic()");
        assertTrue(defaultTasksTest.equalsEpic(epic, savedEpic),
                "Эпики отличаются при сравнивании в addNewEpic()");

        final List<Epic> epics = tasksManager.getEpics();

        assertNotNull(epics, "Эпики не возвращаются в addNewEpic()");
        assertEquals(1, epics.size(), "Неверное количество эпиков в addNewEpic()");
        assertTrue(defaultTasksTest.equalsEpic(epic, epics.getFirst()),
                "Эпики отличаются при сравнивании в addNewEpic()");
    }

    @Test
    void addNewSubtask() {
        tasksManager.addNewEpic(new Epic("Тестовый эпик addNewSubtask()",
                "Описание тестового эпика addNewSubtask()",
                null,
                null));

        SubTask subTask = new SubTask(
                1,
                "Тестовая подзадача addNewSubtask()",
                "Описание тестовой подзадачи addNewSubtask()"
        );

        final int subTaskId = tasksManager.addNewSubtask(1, subTask);
        final SubTask savedSubTask = tasksManager.getSubtask(subTaskId);

        assertNotNull(savedSubTask, "Подзадача не найдена в addNewSubtask()");
        assertTrue(defaultTasksTest.equalsSubTask(subTask, savedSubTask),
                "Подзадачи отличаются при сравнивании в addNewSubtask()");

        final List<SubTask> subTasks = tasksManager.getSubtasks();

        assertNotNull(subTasks, "Подзадачи не возвращаются в addNewSubtask()");
        assertEquals(1, subTasks.size(), "Неверное количество подзадач в addNewSubtask()");
        assertTrue(defaultTasksTest.equalsSubTask(subTask, subTasks.getFirst()),
                "Подзадачи отличаются при сравнивании в addNewSubtask()");
    }

    @Test
    void getTasks() {
        Task task1 = new Task(
                "Тестовая задача #1 getTasks()",
                "Описание тестовой задачи #1 getTasks()"
        );

        Task task2 = new Task(
                "Тестовая задача #2 getTasks()",
                "Описание тестовой задачи #2 getTasks()"
        );

        Task task3 = new Task(
                "Тестовая задача #3 getTasks()",
                "Описание тестовой задачи #3 getTasks()"
        );

        tasksManager.addNewTask(task1);
        tasksManager.addNewTask(task2);
        tasksManager.addNewTask(task3);

        final List<Task> tasks = tasksManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются в getTasks()");
        assertEquals(3, tasks.size(), "Неверное количество задач в getTasks()");
        assertTrue(defaultTasksTest.equalsTask(task1, tasks.get(0)),
                "Задачи #1 отличаются при сравнивании в getTasks()");
        assertTrue(defaultTasksTest.equalsTask(task2, tasks.get(1)),
                "Задачи #2 отличаются при сравнивании в getTasks()");
        assertTrue(defaultTasksTest.equalsTask(task3, tasks.get(2)),
                "Задачи #3 отличаются при сравнивании в getTasks()");
    }

    @Test
    void getSubtasks() {
        tasksManager.addNewEpic(new Epic(
                "Тестовый эпик getSubtasks()",
                "Описание тестового эпика getSubtasks()"
        ));

        SubTask subTask1 = new SubTask(
                1,
                "Тестовая подзадача #1 getSubtasks()",
                "Описание тестовой подзадачи #1 getSubtasks()"
        );

        SubTask subTask2 = new SubTask(
                1,
                "Тестовая подзадача #2 getSubtasks()",
                "Описание тестовой подзадачи #2 getSubtasks()"
        );

        SubTask subTask3 = new SubTask(
                1,
                "Тестовая подзадача #3 getSubtasks()",
                "Описание тестовой подзадачи #3 getSubtasks()"
        );

        tasksManager.addNewSubtask(1, subTask1);
        tasksManager.addNewSubtask(1, subTask2);
        tasksManager.addNewSubtask(1, subTask3);

        final List<SubTask> subTasks = tasksManager.getSubtasks();

        assertNotNull(subTasks, "Подзадачи не возвращаются в getSubtasks()");
        assertEquals(3, subTasks.size(), "Неверное количество подзадач в getSubtasks()");
        assertTrue(defaultTasksTest.equalsSubTask(subTask1, subTasks.get(0)),
                "Подзадачи #1 отличаются при сравнивании в getSubtasks()");
        assertTrue(defaultTasksTest.equalsSubTask(subTask2, subTasks.get(1)),
                "Подзадачи #2 отличаются при сравнивании в getSubtasks()");
        assertTrue(defaultTasksTest.equalsSubTask(subTask3, subTasks.get(2)),
                "Подзадачи #3 отличаются при сравнивании в getSubtasks()");
    }

    @Test
    void getEpics() {
        Epic epic1 = new Epic(
                "Тестовый эпик #1 getEpics()",
                "Описание тестового эпика #1 getEpics()"
        );

        Epic epic2 = new Epic(
                "Тестовый эпик #2 getEpics()",
                "Описание тестового эпика #2 getEpics()"
        );

        Epic epic3 = new Epic(
                "Тестовый эпик #3 getEpics()",
                "Описание тестового эпика #3 getEpics()"
        );

        tasksManager.addNewEpic(epic1);
        tasksManager.addNewEpic(epic2);
        tasksManager.addNewEpic(epic3);

        final List<Epic> epics = tasksManager.getEpics();

        assertNotNull(epics, "Эпики не возвращаются в getEpics()");
        assertEquals(3, epics.size(), "Неверное количество эпиков в getEpics()");
        assertTrue(defaultTasksTest.equalsEpic(epic1, epics.get(0)),
                "Эпики #1 отличаются при сравнивании в getEpics()");
        assertTrue(defaultTasksTest.equalsEpic(epic2, epics.get(1)),
                "Эпики #2 отличаются при сравнивании в getEpics()");
        assertTrue(defaultTasksTest.equalsEpic(epic3, epics.get(2)),
                "Эпики #3 отличаются при сравнивании в getEpics()");
    }

    @Test
    void getEpicSubtasks() {
        tasksManager.addNewEpic(new Epic(
                "Тестовый эпик getEpicSubtasks()",
                "Описание тестового эпика getEpicSubtasks()"
        ));

        SubTask subTask1 = new SubTask(
                1,
                "Тестовая подзадача #1 getEpicSubtasks()",
                "Описание тестовой подзадачи #1 getEpicSubtasks()"
        );

        SubTask subTask2 = new SubTask(
                1,
                "Тестовая подзадача #2 getEpicSubtasks()",
                "Описание тестовой подзадачи #2 getEpicSubtasks()"
        );

        SubTask subTask3 = new SubTask(
                1,
                "Тестовая подзадача #3 getEpicSubtasks()",
                "Описание тестовой подзадачи #3 getEpicSubtasks()"
        );

        tasksManager.addNewSubtask(1, subTask1);
        tasksManager.addNewSubtask(1, subTask2);
        tasksManager.addNewSubtask(1, subTask3);

        final List<SubTask> epicSubtasks = tasksManager.getEpicSubtasks(1);

        assertNotNull(epicSubtasks, "Подзадачи не возвращаются в getEpicSubtasks()");
        assertEquals(3, epicSubtasks.size(), "Неверное количество подзадач в getEpicSubtasks()");
        assertTrue(defaultTasksTest.equalsSubTask(subTask1, epicSubtasks.get(0)),
                "Подзадачи #1 отличаются при сравнивании в getEpicSubtasks()");
        assertTrue(defaultTasksTest.equalsSubTask(subTask2, epicSubtasks.get(1)),
                "Подзадачи #1 отличаются при сравнивании в getEpicSubtasks()");
        assertTrue(defaultTasksTest.equalsSubTask(subTask3, epicSubtasks.get(2)),
                "Подзадачи #1 отличаются при сравнивании в getEpicSubtasks()");
    }

    @Test
    void getEpic() {
        Epic epic = new Epic(
                "Тестовый эпик getEpic()",
                "Описание тестового эпика getEpic()"
        );

        final int epicId = tasksManager.addNewEpic(epic);
        final Epic epicById = tasksManager.getEpic(epicId);

        assertNotNull(epicById, "Эпик не возвращается в getEpic()");
        assertTrue(defaultTasksTest.equalsEpic(epic, epicById),
                "Эпики отличаются при сравнивании в getEpic()");
    }

    @Test
    void getTask() {
        Task task = new Task(
                "Тестовая задача getTask()",
                "Описание тестовой задачи getTask()"
        );

        final int taskId = tasksManager.addNewTask(task);

        final Task taskById = tasksManager.getTask(taskId);

        assertNotNull(taskById, "Задача не возвращаются в getTask()");
        assertTrue(defaultTasksTest.equalsTask(task, taskById),
                "Задачи отличаются при сравнивании в getTask()");
    }

    @Test
    void getSubtask() {
        tasksManager.addNewEpic(new Epic(
                "Тестовый эпик getSubtask()",
                "Описание тестового эпика getSubtask()"
        ));

        SubTask subTask = new SubTask(
                1,
                "Тестовая подзадача getSubtask()",
                "Описание тестовой подзадачи getSubtask()"
        );

        final int subTaskId = tasksManager.addNewSubtask(1, subTask);
        final SubTask subTaskById = tasksManager.getSubtask(subTaskId);

        assertNotNull(subTaskById, "Подзадача не возвращаются в getSubtask()");
        assertTrue(defaultTasksTest.equalsSubTask(subTask, subTaskById),
                "Подзадачи отличаются при сравнивании в getSubtask()");
    }

    @Test
    void updateTask() {
        final int taskId = tasksManager.addNewTask(new Task(
                "Тестовая задача updateTask()",
                "Описание тестовой задачи updateTask()"
        ));

        Task updatedTask = new Task(
                1,
                "Обновлённая тестовая задача updateTask()",
                "Обновлённое описание тестовой задачи updateTask()"
        );

        tasksManager.updateTask(updatedTask);
        final Task taskById = tasksManager.getTask(taskId);

        assertTrue(defaultTasksTest.equalsTask(taskById, updatedTask),
                "Задачи отличаются при сравнивании в updateTask()");
    }

    @Test
    void updateEpic() {
        final int epicId = tasksManager.addNewEpic(new Epic(
                "Тестовый эпик updateEpic()",
                "Описание тестового эпика updateEpic()"
        ));

        Epic updatedEpic = new Epic(
                1,
                "Обновлённый тестовый эпик updateEpic()",
                "Обновлённое описание тестового эпика updateEpic()"
        );

        tasksManager.updateEpic(updatedEpic);
        final Epic epicById = tasksManager.getEpic(epicId);

        assertTrue(defaultTasksTest.equalsEpic(epicById, updatedEpic),
                "Эпики отличаются при сравнивании в updateEpic()");
    }

    @Test
    void updateSubtask() {
        tasksManager.addNewEpic(new Epic(
                "Тестовый эпик updateSubtask()",
                "Описание тестового эпика updateSubtask()"
        ));

        final int subTaskId = tasksManager.addNewSubtask(
                1,
                (new SubTask(
                        1,
                        "Тестовая подзадача updateSubtask()",
                        "Описание тестовой подзадачи updateSubtask()"
                )));

        SubTask updatedSubTask = new SubTask(
                2,
                1,
                "Обновлённая тестовая подзадача updateSubtask()",
                "Обновлённое описание тестовой подзадачи updateSubtask()",
                Status.IN_PROGRESS
        );

        tasksManager.updateSubtask(updatedSubTask);
        final SubTask subTaskById = tasksManager.getSubtask(subTaskId);

        assertTrue(defaultTasksTest.equalsSubTask(subTaskById, updatedSubTask),
                "Подзадачи отличаются при сравнивании в updateSubtask()");
    }

    @Test
    void deleteTask() {
        final int taskId = tasksManager.addNewTask(new Task(
                "Тестовая задача #1 deleteTask()",
                "Описание тестовой задачи #1 deleteTask()"
        ));

        Task task2 = new Task(
                "Тестовая задача #2 deleteTask()",
                "Описание тестовой задачи #2 deleteTask()"
        );

        tasksManager.addNewTask(task2);

        tasksManager.deleteTask(taskId);
        final List<Task> tasks = tasksManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются в deleteTask()");
        assertEquals(1, tasks.size(), "Неверное количество задач в deleteTask()");
        assertEquals(task2.getName(), tasks.getFirst().getName(), "Задачи не совпадают в deleteTask()");
        assertTrue(defaultTasksTest.equalsTask(task2, tasks.getFirst()),
                "Задачи отличаются при сравнивании в deleteTask()");
    }

    @Test
    void deleteEpic() {
        final int epicId = tasksManager.addNewEpic(new Epic(
                "Тестовый эпик #1 deleteEpic()",
                "Описание тестового эпика #1 deleteEpic()"
        ));

        Epic epic2 = new Epic(
                "Тестовый эпик #2 deleteEpic()",
                "Описание тестового эпика #2 deleteEpic()"
        );

        tasksManager.addNewEpic(epic2);

        tasksManager.deleteEpic(epicId);
        final List<Epic> epics = tasksManager.getEpics();

        assertNotNull(epics, "Эпики не возвращаются в deleteEpic()");
        assertEquals(1, epics.size(), "Неверное количество Эпиков в deleteEpic()");
        assertEquals(epic2.getName(), epics.getFirst().getName(), "Эпики не совпадают в deleteEpic()");
        assertTrue(defaultTasksTest.equalsEpic(epic2, epics.getFirst()),
                "Эпики отличаются при сравнивании в deleteEpic()");
    }

    @Test
    void deleteSubtask() {
        tasksManager.addNewEpic(new Epic(
                "Тестовый эпик deleteSubtask()",
                "Описание тестового эпика deleteSubtask()"
        ));

        final int subTaskId = tasksManager.addNewSubtask(
                1,
                (new SubTask(
                        1,
                        "Тестовая подзадача #1 deleteSubtask()",
                        "Описание тестовой подзадачи #1 deleteSubtask()"
                )));

        SubTask subTask2 = new SubTask(
                1,
                "Тестовая подзадача #2 deleteSubtask()",
                "Описание тестовой подзадачи #2 deleteSubtask()"
        );

        tasksManager.addNewSubtask(1, subTask2);

        tasksManager.deleteSubtask(subTaskId);
        final List<SubTask> subTasks = tasksManager.getSubtasks();

        assertNotNull(subTasks, "Подзадачи не возвращаются в deleteSubtask()");
        assertEquals(1, subTasks.size(), "Неверное количество подзадач в deleteSubtask()");
        assertTrue(defaultTasksTest.equalsSubTask(subTask2, subTasks.getFirst()),
                "Подзадачи отличаются при сравнивании в deleteSubtask()");
    }

    @Test
    void deleteTasks() {
        tasksManager.addNewTask(new Task(
                "Тестовая задача #1 deleteTasks()",
                "Описание тестовой задачи #1 deleteTasks()"
        ));

        tasksManager.addNewTask(new Task(
                "Тестовая задача #2 deleteTasks()",
                "Описание тестовой задачи #2 deleteTasks()"
        ));

        tasksManager.addNewTask(new Task(
                "Тестовая задача #3 deleteTasks()",
                "Описание тестовой задачи #3 deleteTasks()"
        ));

        tasksManager.deleteTasks();
        final List<Task> tasks = tasksManager.getTasks();

        assertEquals(0, tasks.size(), "Задачи не были удалены из менеджера в deleteTasks()");
    }

    @Test
    void deleteSubtasks() {
        tasksManager.addNewEpic(new Epic(
                "Тестовый эпик #1 deleteSubtasks()",
                "Описание тестового эпика #1 deleteSubtasks()"
        ));

        tasksManager.addNewSubtask(
                1,
                (new SubTask(
                        1,
                        "Тестовая подзадача #1 deleteSubtasks()",
                        "Описание тестовой подзадачи #1 deleteSubtasks()"
                )));

        tasksManager.addNewSubtask(
                1,
                (new SubTask(
                        1,
                        "Тестовая подзадача #2 deleteSubtasks()",
                        "Описание тестовой подзадачи #2 deleteSubtasks()"
                )));

        tasksManager.addNewEpic(new Epic(
                "Тестовый эпик #2 deleteSubtasks()",
                "Описание тестового эпика #2 deleteSubtasks()"
        ));
        tasksManager.addNewSubtask(
                4,
                (new SubTask(
                        4,
                        "Тестовая подзадача #3 deleteSubtasks()",
                        "Описание тестовой подзадачи #3 deleteSubtasks()"
                )));

        tasksManager.deleteSubtasks();
        final List<SubTask> subTasks = tasksManager.getSubtasks();
        final List<Epic> epics = tasksManager.getEpics();

        assertEquals(0, subTasks.size(), "Подзадачи не были удалены в deleteSubtasks()");
        assertEquals(2, epics.size(), "Эпики пропали после удаления подзадач в deleteSubtasks()");
    }

    @Test
    void deleteEpics() {
        tasksManager.addNewEpic(new Epic(
                "Тестовый эпик #1 deleteEpics()",
                "Описание тестового эпика #1 deleteEpics()"
        ));

        tasksManager.addNewEpic(new Epic(
                "Тестовый эпик #2 deleteEpics()",
                "Описание тестового эпика #2 deleteEpics()"
        ));

        tasksManager.addNewEpic(new Epic(
                "Тестовый эпик #3 deleteEpics()",
                "Описание тестового эпика #3 deleteEpics()"
        ));

        tasksManager.deleteEpics();
        final List<Epic> epics = tasksManager.getEpics();
        final List<SubTask> subTasks = tasksManager.getSubtasks();

        assertEquals(0, epics.size(), "Эпики не были удалены в deleteEpics()");
        assertEquals(0, subTasks.size(),
                "Подзадачи не удалились при удалении всех эпиков в deleteEpics()");
    }
}