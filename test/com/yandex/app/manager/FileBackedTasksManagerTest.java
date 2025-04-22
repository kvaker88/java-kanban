package com.yandex.app.manager;

import com.yandex.app.task.Epic;
import com.yandex.app.task.Status;
import com.yandex.app.task.SubTask;
import com.yandex.app.task.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TasksManagerTest<FileBackedTasksManager> {

    @Test
    void saveAndLoadEmptyFile() throws IOException {
        File file = File.createTempFile("save_", ".csv");

        FileBackedTasksManager fileBackedTasksManager1 = new FileBackedTasksManager(file);

        Assertions.assertTrue(fileBackedTasksManager1.getTasks().isEmpty());
        Assertions.assertTrue(fileBackedTasksManager1.getEpics().isEmpty());
        Assertions.assertTrue(fileBackedTasksManager1.getSubtasks().isEmpty());

        fileBackedTasksManager1.addNewTask(new Task(
                1,
                "Task saveAndLoadEmptyFile",
                "saveAndLoadEmptyFile Description",
                Status.IN_PROGRESS,
                Duration.ofMinutes(15),
                LocalDateTime.of(1970,1,1,0,0)
        ));

        FileBackedTasksManager fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(file);
        String taskToString = fileBackedTasksManager2.getTask(1).toStringToFile();
        assertEquals(taskToString,
                "1," +
                        "TASK," +
                        "Task saveAndLoadEmptyFile," +
                        "IN_PROGRESS," +
                        "saveAndLoadEmptyFile Description," +
                        "1970-01-01T00:00," +
                        "PT15M\n",
                "Содержимое не совпадает");
    }

    @Test
    void save() throws IOException {
        File file = File.createTempFile("save_", ".csv");

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        Task taskForTestSave = new Task(
                1,
                "Task save",
                "save Description"
        );
        fileBackedTasksManager.addNewTask(taskForTestSave);
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
    void loadFromFile() throws IOException {
        File file = File.createTempFile("save_", ".csv");
        FileBackedTasksManager fileBackedTasksManager1 = new FileBackedTasksManager(file);

        Task task = new Task(
                1,
                "Task loadFromFile",
                "loadFromFile Description"
        );

        Epic epic = new Epic(
                1,
                "Epic loadFromFile",
                "loadFromFile Description"
        );

        SubTask subTask = new SubTask(
                1,
                2,
                "SubTask loadFromFile",
                "loadFromFile Description",
                Status.NEW
        );

        fileBackedTasksManager1.addNewTask(task);
        fileBackedTasksManager1.addNewEpic(epic);
        fileBackedTasksManager1.addNewSubtask(2, subTask);

        String taskToString1 = fileBackedTasksManager1.tasks.get(1).toStringToFile();
        String epicToString1 = fileBackedTasksManager1.epics.get(2).toStringToFile();
        String subTaskToString1 = fileBackedTasksManager1.subtasks.get(3).toStringToFile();
        String epicSubTasksToString1 = fileBackedTasksManager1.epics.get(2).getSubtasks().toString();

        // указываем путь до файла, создаём второй менеджер с использованием метода загрузки из ранее созданного файла
        FileBackedTasksManager fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(file);
        String taskToString2 = fileBackedTasksManager2.getTask(1).toStringToFile();
        String epicToString2 = fileBackedTasksManager2.getEpic(2).toStringToFile();
        String subTaskToString2 = fileBackedTasksManager2.getSubtask(3).toStringToFile();
        String epicSubTasksToString2 = fileBackedTasksManager2.epics.get(2).getSubtasks().toString();

        // Проверяем совпадение задач из двух менеджеров
        assertEquals(taskToString1, taskToString2, "Задача из загруженного файла не совпадает.");
        assertEquals(epicToString1, epicToString2, "Эпик из загруженного файла не совпадает.");
        assertEquals(subTaskToString1, subTaskToString2, "Подзадача из загруженного файла не совпадает.");
        assertEquals(epicSubTasksToString1, epicSubTasksToString2, "Подзадачи у эпика не совпадают.");
    }
}