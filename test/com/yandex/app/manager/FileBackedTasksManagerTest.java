package com.yandex.app.manager;

import com.yandex.app.task.Epic;
import com.yandex.app.task.Status;
import com.yandex.app.task.SubTask;
import com.yandex.app.task.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TasksManagerTest<FileBackedTasksManager> {

    private File file;

    @BeforeEach
    public void setUp() throws IOException {
        file = File.createTempFile("prak", "csv");
        tasksManager = new FileBackedTasksManager(file);
    }

    @Test
    void saveAndLoadEmptyFile() {
        Assertions.assertTrue(tasksManager.getTasks().isEmpty());
        Assertions.assertTrue(tasksManager.getEpics().isEmpty());
        Assertions.assertTrue(tasksManager.getSubtasks().isEmpty());

        tasksManager.addNewTask(new Task(
                1,
                "Task saveAndLoadEmptyFile",
                "saveAndLoadEmptyFile Description",
                Status.IN_PROGRESS,
                Duration.ofMinutes(15),
                LocalDateTime.of(1970, 1, 1, 0, 0)
        ));

        FileBackedTasksManager fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(file);
        String taskToString = fileBackedTasksManager2.getTask(1).toStringToFile();
        assertEquals(taskToString,
                "1," +
                        "TASK," +
                        "Task saveAndLoadEmptyFile," +
                        "IN_PROGRESS," +
                        "saveAndLoadEmptyFile Description," +
                        "," +
                        "1970-01-01T00:00," +
                        "PT15M\n",
                "Содержимое не совпадает");
    }

    @Test
    void save() {
        Task taskForTestSave = new Task(
                1,
                "Task save",
                "save Description"
        );
        tasksManager.addNewTask(taskForTestSave);
        String taskToString = taskForTestSave.toStringToFile();

        String line = "";
        try (Reader fileReader = new FileReader(file)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine();

            line = bufferedReader.readLine() + "\n";
            bufferedReader.close();
        } catch (IOException exception) {
            System.out.println("Не удалось прочитать файл.");
        }

        assertEquals(taskToString, line, "Содержимое файла не совпадает с задачей");
    }

    @Test
    void loadFromFile() {
        Task manager1Task1 = new Task(
                1,
                "Task#1 loadFromFile",
                "loadFromFile Description#1",
                Status.IN_PROGRESS,
                Duration.ofMinutes(15),
                LocalDateTime.of(1970, 1, 1, 0, 0)
        );
        Task manager1Task2 = new Task(
                2,
                "Task#2 loadFromFile",
                "loadFromFile Description#2",
                Status.IN_PROGRESS,
                Duration.ofMinutes(15),
                LocalDateTime.of(1970, 1, 1, 0, 15)
        );
        Task manager1Task3 = new Task(
                3,
                "Task#3 loadFromFile",
                "loadFromFile Description#3"
        );

        Epic manager1Epic1 = new Epic(
                4,
                "Epic#1 loadFromFile",
                "loadFromFile Description#4"
        );

        SubTask manager1SubTask1 = new SubTask(
                5,
                4,
                "SubTask#1 loadFromFile",
                "loadFromFile Description#5",
                Status.IN_PROGRESS,
                Duration.ofMinutes(15),
                LocalDateTime.of(1970, 1, 1, 0, 30)
        );
        SubTask manager1SubTask2 = new SubTask(
                6,
                4,
                "SubTask#2 loadFromFile",
                "loadFromFile Description#6",
                Status.NEW
        );

        tasksManager.addNewTask(manager1Task1);
        tasksManager.addNewTask(manager1Task2);
        tasksManager.addNewTask(manager1Task3);
        tasksManager.addNewEpic(manager1Epic1);
        tasksManager.addNewSubtask(4, manager1SubTask1);
        tasksManager.addNewSubtask(4, manager1SubTask2);

        FileBackedTasksManager tasksManager2 = FileBackedTasksManager.loadFromFile(file);

        Task manager2Task1 = tasksManager2.tasks.get(1);
        Task manager2Task2 = tasksManager2.tasks.get(2);
        Task manager2Task3 = tasksManager2.tasks.get(3);

        Epic manager2Epic1 = tasksManager2.epics.get(4);

        SubTask manager2SubTask1 = tasksManager2.subtasks.get(5);
        SubTask manager2SubTask2 = tasksManager2.subtasks.get(6);

        assertEquals(manager1Task1.getId(), manager2Task1.getId(),
                "ID в первой задаче не совпадает.");
        assertEquals(manager1Task2.getId(), manager2Task2.getId(),
                "ID во второй задаче не совпадает.");
        assertEquals(manager1Task3.getId(), manager2Task3.getId(),
                "ID в третей задаче не совпадает.");

        assertEquals(manager1Epic1.getId(), manager2Epic1.getId(),
                "ID эпика не совпадает.");

        assertEquals(manager1SubTask1.getId(), manager2SubTask1.getId(),
                "ID в первой подзадаче не совпадает.");
        assertEquals(manager1SubTask2.getId(), manager2SubTask2.getId(),
                "ID в первой подзадаче не совпадает.");
        assertEquals(manager1SubTask1.getEpicId(), manager2SubTask1.getEpicId(),
                "ID эпика в первой подзадаче не совпадает.");
        assertEquals(manager1SubTask2.getEpicId(), manager2SubTask2.getEpicId(),
                "ID эпика в первой подзадаче не совпадает.");

        assertEquals(manager1Task1.getName(), manager2Task1.getName(),
                "Имя в первой задаче не совпадает.");
        assertEquals(manager1Task2.getName(), manager2Task2.getName(),
                "Имя во второй задаче не совпадает.");
        assertEquals(manager1Task3.getName(), manager2Task3.getName(),
                "Имя в третей задаче не совпадает.");

        assertEquals(manager1Epic1.getName(), manager2Epic1.getName(),
                "Имя эпика не совпадает.");

        assertEquals(manager1Task1.getName(), manager2Task1.getName(),
                "Имя в первой подзадаче не совпадает.");
        assertEquals(manager1Task1.getName(), manager2Task1.getName(),
                "Имя в первой подзадаче не совпадает.");

        assertEquals(manager1Task1.getDescription(), manager2Task1.getDescription(),
                "Описание в первой задаче не совпадает.");
        assertEquals(manager1Task2.getDescription(), manager2Task2.getDescription(),
                "Описание во второй задаче не совпадает.");
        assertEquals(manager1Task3.getDescription(), manager2Task3.getDescription(),
                "Описание в третей задаче не совпадает.");

        assertEquals(manager1Epic1.getDescription(), manager2Epic1.getDescription(),
                "Описание эпика не совпадает.");

        assertEquals(manager1Task1.getDescription(), manager2Task1.getDescription(),
                "Описание в первой подзадаче не совпадает.");
        assertEquals(manager1Task1.getDescription(), manager2Task1.getDescription(),
                "Описание в первой подзадаче не совпадает.");

        assertEquals(manager1Task1.getStatus(), manager2Task1.getStatus(),
                "Статус в первой задаче не совпадает.");
        assertEquals(manager1Task2.getStatus(), manager2Task2.getStatus(),
                "Статус во второй задаче не совпадает.");
        assertEquals(manager1Task3.getStatus(), manager2Task3.getStatus(),
                "Статус в третей задаче не совпадает.");

        assertEquals(manager1Epic1.getStatus(), manager2Epic1.getStatus(),
                "Статус эпика не совпадает.");

        assertEquals(manager1Task1.getStatus(), manager2Task1.getStatus(),
                "Статус в первой подзадаче не совпадает.");
        assertEquals(manager1Task1.getStatus(), manager2Task1.getStatus(),
                "Статус в первой подзадаче не совпадает.");

        assertEquals(manager1Task1.getStartTime(), manager2Task1.getStartTime(),
                "Дата начала в первой задаче не совпадает.");
        assertEquals(manager1Task2.getStartTime(), manager2Task2.getStartTime(),
                "Дата начала во второй задаче не совпадает.");
        assertEquals(manager1Task3.getStartTime(), manager2Task3.getStartTime(),
                "Дата начала в третей задаче не совпадает.");

        assertEquals(manager1Epic1.getStartTime(), manager2Epic1.getStartTime(),
                "Дата начала эпика не совпадает.");
        assertEquals(manager1Epic1.getEndTime(), manager2Epic1.getEndTime(),
                "Дата окончания эпика не совпадает.");

        assertEquals(manager1Task1.getStartTime(), manager2Task1.getStartTime(),
                "Дата начала в первой подзадаче не совпадает.");
        assertEquals(manager1Task1.getStartTime(), manager2Task1.getStartTime(),
                "Дата начала в первой подзадаче не совпадает.");

        assertEquals(manager1Task1.getDuration(), manager2Task1.getDuration(),
                "Длительность в первой задаче не совпадает.");
        assertEquals(manager1Task2.getDuration(), manager2Task2.getDuration(),
                "Длительность во второй задаче не совпадает.");
        assertEquals(manager1Task3.getDuration(), manager2Task3.getDuration(),
                "Длительность в третей задаче не совпадает.");

        assertEquals(manager1Epic1.getDuration(), manager2Epic1.getDuration(),
                "Длительность эпика не совпадает.");

        assertEquals(manager1Task1.getDuration(), manager2Task1.getDuration(),
                "Длительность в первой подзадаче не совпадает.");
        assertEquals(manager1Task1.getDuration(), manager2Task1.getDuration(),
                "Длительность в первой подзадаче не совпадает.");

        List<Task> expectedPriorityList = tasksManager.getPrioritizedTasks();
        List<Task> restoredPriorityList = tasksManager2.getPrioritizedTasks();

        for (int i = 0; i < expectedPriorityList.size(); i++) {
            Task expectedTask = expectedPriorityList.get(i);
            Task restoredTask = restoredPriorityList.get(i);
            assertEquals(expectedTask.getId(), restoredTask.getId(),
                    "Задачи в списке приоритетов не совпадают на позиции " + i);
            assertEquals(expectedTask.getStartTime(), restoredTask.getStartTime(),
                    "Не совпадает startTime задачи в сортированном списке на позиции " + i);
        }
    }
}