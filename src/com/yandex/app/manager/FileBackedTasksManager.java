package com.yandex.app.manager;

import com.yandex.app.exceptions.ManagerSaveException;
import com.yandex.app.task.*;

import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTasksManager {

    private static final String HEADER = "id,type,name,status,description,epic\n";
    private static final Path PATH = Path.of(".\\");

        /* Немного изменил логику передачи задач, чтобы вместе с ними передавались упорядоченные ID.
        Нужно это для того, чтобы HashMap всегда передавал упорядоченные задачи любого количества на сохранение,
        чтобы исключить вариант перемешивания ID и чтобы при загрузке он всегда устанавливался больше на единицу.
        Изменения задели методы groupAllTasksToString и allTasksToString. */

    private String groupAllTasksToString() { // разбил методы для скрытия работы кода
        Map<Integer, Task> allTasks = new HashMap<>();
        allTasks.putAll(tasks);
        allTasks.putAll(epics);
        allTasks.putAll(subtasks);

        Object[] arr = allTasks.keySet().toArray(); // создаём массив ID по ключам мапы
        Arrays.sort(arr); // сортируем

        return allTasksToString(List.of(arr), allTasks); // передаём массив и мапу с задачами для записи в String

    }

    private String allTasksToString(List<Object> arr, Map<Integer, Task> allTasks) {
        StringBuilder tasksString = new StringBuilder();

        for (Object id : arr) {
            int idToInt = Integer.parseInt(id.toString()); // перезаписываем ID из Object в int
            tasksString.append(allTasks.get(idToInt).toStringToFile()); // добавляем задачу по полученному ID в строку
        }
        return tasksString.toString(); // отдаём готовую строку со всеми задачами
    }

    private void save() { // метод для сохранения существующих задач всех типов в файл
        try (Writer fileWriter = new FileWriter(PATH + "\\save.csv", false)) {
            fileWriter.write(HEADER); // дефолтная первая строка с пояснением
            fileWriter.write(groupAllTasksToString());
        } catch (IOException exception) {
            throw new ManagerSaveException("Не удалось записать файл.");
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        try (Reader fileReader = new FileReader(file)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine(); // пропускаем первую строку

            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (!line.equals("")) {
                    addTaskFromFile(line.split(","), fileBackedTasksManager);
                }
                fileBackedTasksManager.id = Integer.parseInt(String.valueOf(line.charAt(0))) + 1; // устанавливаем ID последней задачи
            }
            bufferedReader.close();
        } catch (IOException exception) {
            System.out.println("Не удалось прочитать файл.");
        }
        return fileBackedTasksManager;
    }

    private static void addTaskFromFile(String[] taskArr, FileBackedTasksManager fileBackedTasksManager) {
        // текущий метод создаёт пустые задачи нужного типа по первому индексу каждой строки файла
        switch (Type.valueOf(taskArr[1])) {
            case TASK: {
                fileBackedTasksManager.tasks.put(Integer.parseInt(taskArr[0]), // добавляем задачу напрямую в мапу
                        new Task(Integer.parseInt(taskArr[0]), // добавляем айди задачи
                        taskArr[2], // добавляем имя
                        taskArr[4], // добавляем описание
                        Status.valueOf(taskArr[3]))); // устанавливаем статус
                break;
            }

            case EPIC: {
                fileBackedTasksManager.epics.put(Integer.parseInt(taskArr[0]), // добавляем задачу напрямую в мапу
                        new Epic(Integer.parseInt(taskArr[0]), // добавляем айди задачи
                        taskArr[2], // добавляем имя
                        taskArr[4], // добавляем описание
                        Status.valueOf(taskArr[3]))); // устанавливаем статус
                break;
            }

            case SUBTASK: {
                SubTask subTask = new SubTask(Integer.parseInt(taskArr[0]), // добавляем айди задачи
                        Integer.parseInt(taskArr[5]), // ещё раз добвляем айди эпика
                        taskArr[2], // добавляем имя
                        taskArr[4], // добавляем описание
                        Status.valueOf(taskArr[3]));// устанавливаем статус

                fileBackedTasksManager.subtasks.put(Integer.parseInt(taskArr[0]), subTask);
                // добавляем подзадачу с айди эпика напрямую в мапу

                fileBackedTasksManager.epics.get(Integer.parseInt(taskArr[5])).addSubTask(subTask);
                // добавляем связь между эпиком и подзадачей
                break;
            }
        }
    }

    @Override
    public int addNewTask(Task task) {
        super.addNewTask(task);
        save();
        return task.getId();
    }

    @Override
    public int addNewEpic(Epic epic) {
        super.addNewEpic(epic);
        save();
        return epic.getId();
    }

    @Override
    public int addNewSubtask(int epicId, SubTask subTask) {
        super.addNewSubtask(epicId, subTask);
        save();
        return subTask.getId();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(SubTask subTask) {
        super.updateSubtask(subTask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }
}

