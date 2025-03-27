package com.yandex.app.manager;

import com.yandex.app.task.Epic;
import com.yandex.app.task.Status;
import com.yandex.app.task.SubTask;
import com.yandex.app.task.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest {

    @Test
    void saveAndLoadEmptyFile() throws IOException {
        File file = File.createTempFile("save_", ".csv");

        FileBackedTasksManager fileBackedTasksManager1 = new FileBackedTasksManager(file);
        fileBackedTasksManager1.loadFromFile(file);

        Assertions.assertTrue(fileBackedTasksManager1.getTasks().isEmpty());
        Assertions.assertTrue(fileBackedTasksManager1.getEpics().isEmpty());
        Assertions.assertTrue(fileBackedTasksManager1.getSubtasks().isEmpty());

        fileBackedTasksManager1.addNewTask(new Task(1, "Task", "Description"));

        FileBackedTasksManager fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(file);
        String taskToString = fileBackedTasksManager2.getTask(1).toStringToFile();
        assertEquals(taskToString, "1,TASK,Task,NEW,Description\n", "Содержимое не совпадает");
    }

    @Test
    void save() throws IOException {
        File file = File.createTempFile("save_", ".csv");

        // создаём менеджер, в который добавляем одну задачу, методом addNewTask создаётся файл save.csv
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        Task taskForTestSave = new Task(1,"Task1", "Description task1");
        fileBackedTasksManager.addNewTask(taskForTestSave);
        String taskToString = taskForTestSave.toStringToFile();

        // читаем содержимое файла с помощью BuffeeredReader, записываем в ранее созданную переменную line
        String line = "";
        try (Reader fileReader = new FileReader(file)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine(); // пропускаем первую строку

            line = bufferedReader.readLine() + "\n";
            // добавляем перенос строки, так как task.toStringToFile()
            // тоже это делает и разница в переносах не даст пройти тесту
            bufferedReader.close(); // закрываем чтение
        } catch (IOException exception) {
            System.out.println("Не удалось прочитать файл.");
        }
        // проверяем, что файл содержит ту информацию, которую мы передавали в save
        assertEquals(taskToString, line, "Содержимое файла не совпадает с задачей");
    }

    @Test
    void loadFromFile() throws IOException {
        File file = File.createTempFile("save_", ".csv");

        // Изменил тестирование метода. Вроде каждая задача будет сверена корректно между менеджерами.
        FileBackedTasksManager fileBackedTasksManager1 = new FileBackedTasksManager(file);

        Task task = new Task(1, "Task", "Description forTask");
        Epic epic = new Epic(1, "Epic", "Description forTask");
        SubTask subTask = new SubTask(1,2,"SubTask", "Description forSubTask", Status.NEW);

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
        assertEquals(epicSubTasksToString1, epicSubTasksToString2,"Подзадачи у эпика не совпадают.");
    }
}