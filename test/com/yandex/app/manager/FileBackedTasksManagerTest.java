package com.yandex.app.manager;

import com.yandex.app.DefaultTasksTest;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileBackedTasksManagerTest extends TasksManagerTest<FileBackedTasksManager> {

    private File file;
    DefaultTasksTest defaultTest = new DefaultTasksTest();

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

        assertTrue(defaultTest.equalsTask(manager1Task1, manager2Task1),
                "Задачи №1 не совпадают в loadFromFile()");
        assertTrue(defaultTest.equalsTask(manager1Task2, manager2Task2),
                "Задачи №2 не совпадают в loadFromFile()");
        assertTrue(defaultTest.equalsTask(manager1Task3, manager2Task3),
                "Задачи №3 не совпадают в loadFromFile()");
        assertTrue(defaultTest.equalsEpic(manager1Epic1, manager2Epic1),
                "Эпики не совпадают в loadFromFile()");
        assertTrue(defaultTest.equalsSubTask(manager1SubTask1, manager2SubTask1),
                "Подзадачи №1 не совпадают в loadFromFile()");
        assertTrue(defaultTest.equalsSubTask(manager1SubTask2, manager2SubTask2),
                "Подзадачи №2 не совпадают в loadFromFile()");

        List<Task> expectedPriorityList = tasksManager.getPrioritizedTasks();
        List<Task> restoredPriorityList = tasksManager2.getPrioritizedTasks();

        for (int i = 0; i < expectedPriorityList.size(); i++) {
            Task expectedTask = expectedPriorityList.get(i);
            Task restoredTask = restoredPriorityList.get(i);
            assertTrue(defaultTest.equalsTask(expectedTask, restoredTask),
                    STR."Задачи не совпадают на позиции \{i} в loadFromFile()");
        }
    }
}