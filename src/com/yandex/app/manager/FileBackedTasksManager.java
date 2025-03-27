package com.yandex.app.manager;

import com.yandex.app.exceptions.ManagerSaveException;
import com.yandex.app.task.*;

import java.io.*;


public class FileBackedTasksManager extends InMemoryTasksManager {

    private static final String HEADER = "id,type,name,status,description,epic\n";

    File file;
    public FileBackedTasksManager (File file) {
        this.file = file; // этот конструктор нужен для метода save()
    }


    private String groupAllTasksToString() { // упростил логику обратно, убрал лишние действия
        StringBuilder allTasksToString = new StringBuilder();
        for (Task task : tasks.values()) {
            allTasksToString.append(task.toStringToFile());
        }

        for (Epic epic : epics.values()) {
            allTasksToString.append(epic.toStringToFile());
        }

        for (SubTask subTask : subtasks.values()) {
            allTasksToString.append(subTask.toStringToFile());
        }

        return allTasksToString.toString(); // отдаём сразу готовую строку

    }

    private void save() { // метод для сохранения существующих задач всех типов в файл
        try (Writer fileWriter = new FileWriter(file, false)) {
            fileWriter.write(HEADER); // дефолтная первая строка с пояснением
            fileWriter.write(groupAllTasksToString());
        } catch (IOException exception) {
            throw new ManagerSaveException("Не удалось записать файл.");
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try (Reader fileReader = new FileReader(file)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine(); // пропускаем первую строку

            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (!line.equals("")) {
                    addTaskFromFile(line.split(","), fileBackedTasksManager);
                }
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
                fileBackedTasksManager.id++;
                break;
            }

            case EPIC: {
                fileBackedTasksManager.epics.put(Integer.parseInt(taskArr[0]), // добавляем задачу напрямую в мапу
                        new Epic(Integer.parseInt(taskArr[0]), // добавляем айди задачи
                        taskArr[2], // добавляем имя
                        taskArr[4], // добавляем описание
                        Status.valueOf(taskArr[3]))); // устанавливаем статус
                fileBackedTasksManager.id++;
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

                if (fileBackedTasksManager.epics.containsKey(Integer.parseInt(taskArr[5]))) {
                    fileBackedTasksManager.epics.get(Integer.parseInt(taskArr[5])).addSubTask(subTask);
                    // добавляем связь между эпиком и подзадачей, если эпик существует.
                } else {
                    System.out.println("Подзадача с ID" + (Integer.parseInt(taskArr[0])) +
                            " не добавлена, так как не был найден Epic.");
                }
                fileBackedTasksManager.id++;
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

