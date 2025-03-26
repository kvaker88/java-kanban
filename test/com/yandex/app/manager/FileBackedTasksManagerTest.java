package com.yandex.app.manager;

import com.yandex.app.task.Task;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest {

    // тут заранее напишу. Я не понял как использовать "File.createTempFile()" в данном случае. Метод save() создаёт
    // файл в конкретном месте, не возвращая даже название файла, а для тестов нужен конкретный путь. Можно было бы
    // чуть поменять логику, но это странно делать только для проведения тестов. Поэтому, в последнем тесте добавил
    // удаление файла, чтобы не засорять диск.

    @Test
    void saveAndLoadEmptyFile() {
        // не придумал как иначе реализовать тестирование такого случая.
        // По идее, если файл пуст, никаких задач во второй менеджер добавлено не будет.

        // создаём менеджер, пробуем удалить задачу, чтобы создать файл сохранения. Получаем ответ
        // "Задача с указанным ID не найдена.", но при этом, файл создаётся.
        FileBackedTasksManager fileBackedTasksManager1 = new FileBackedTasksManager();
        fileBackedTasksManager1.deleteTasks();

        // указываем путь до файла, созхдаём второй менеджер с использованием метода загрузки из ранее созданного файла,
        // затем все задачи закидываем в одну мапу и сравниваем с пустой мапой.
        File file = new File(".\\save.csv");
        FileBackedTasksManager fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(file);

        Map<Integer, Task> allTasks = new HashMap<>(fileBackedTasksManager2.tasks);
        allTasks.putAll(fileBackedTasksManager2.tasks);
        allTasks.putAll(fileBackedTasksManager2.epics);
        allTasks.putAll(fileBackedTasksManager2.subtasks);

        Map<Integer, Task> emptyMap = new HashMap<>();

        assertEquals(emptyMap, allTasks, "Задачи есть в файле");
    }

    @Test
    void save() {
        // создаём менеджер, в который добавляем одну задачу, методом addNewTask создаётся файл save.csv
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        Task taskForTestSave = new Task("Task1", "Description task1");
        fileBackedTasksManager.addNewTask(taskForTestSave);
        String taskToString = taskForTestSave.toStringToFile();

        // читаем содержимое файла с помощью BuffeeredReader, записываем в ранее созданную переменную line
        String line = "";
        File file = new File(".\\save.csv");
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
    void loadFromFile() {
        // создаём менеджер, в который добавляем одну задачу, методом addNewTask создаётся файл save.csv
        FileBackedTasksManager fileBackedTasksManager1 = new FileBackedTasksManager();
        Task taskForTestLoadFromFile = new Task(1, "TaskForTestLoadFromFile",
                "Description ForTestLoadFromFile");
        fileBackedTasksManager1.addNewTask(taskForTestLoadFromFile);
        Task task1 = fileBackedTasksManager1.tasks.get(1);

        // указываем путь до файла, создаём второй менеджер с использованием метода загрузки из ранее созданного файла
        File file = new File(".\\save.csv");
        FileBackedTasksManager fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(file);

        Task task2 = fileBackedTasksManager2.getTask(1);

        // Проверяем совпадение задач из двух менеджеров
        assertEquals(task1, task2, "Задача из загруженного файла не совпадает.");
        file.delete(); // чтоб файл лишний не оставался после теста
    }
}