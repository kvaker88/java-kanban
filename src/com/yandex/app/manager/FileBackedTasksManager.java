package com.yandex.app.manager;

import com.yandex.app.exeptions.ManagerSaveException;
import com.yandex.app.task.*;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTasksManager {

    // Создавать файл во временной директории тут не стал, чтобы файл не удалялся после завершения работы программы
    private final Path path = Path.of("C:\\Users\\tihon\\IdeaProjects\\java-kanban\\");

    private String groupAllTasksToString() { // разбил методы для скрытия работы кода
        Map<Integer, Task> allTasks = new HashMap<>();
        allTasks.putAll(tasks);
        allTasks.putAll(epics);
        allTasks.putAll(subtasks);

        return allTasksToString(allTasks);
    }

    private String allTasksToString(Map<Integer, Task> tasks) {
        StringBuilder tasksString = new StringBuilder();

        for (Task task : tasks.values()) {
            tasksString.append(task.toStringToFile());
        }
        return tasksString.toString();
    }

    public void save() { // метод для сохранения существующих задач всех типов в файл
        try (Writer fileWriter = new FileWriter(path + "\\save.csv", false)) {
            fileWriter.write("id,type,name,status,description,epic\n"); // дефолтная первая строка с пояснением
            fileWriter.write(groupAllTasksToString());
        } catch (IOException exception) {
            throw new ManagerSaveException("Не удалось записать файл.");
        }
    }

    public void loadFromFile(File file) {
        // не понимаю как тут реализовать static
        // если поменять модификатор, нужно будет изменить его в addTaskFromFile и, соответственно,
        // во всех созданиях задач основного менеджера, что не круто
        try (Reader fileReader = new FileReader(file)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine();

            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (!line.equals("")) {
                    addTaskFromFile(line.split(","));
                }
            }
        } catch (IOException exception) {
            System.out.println("Не удалось прочитать файл.");
        }
    }

    private void addTaskFromFile(String[] taskArr) { // разбил метод на два, чтобы сократить кол-во кода
        // текущий метод создаёт пустые задачи нужного типа по первому индексу каждой строки файла
        switch (Type.valueOf(taskArr[1])) {
            case TASK: {
                Task task = new Task();
                addNewTask(taskEditor(taskArr, task));
                break;
            }

            case EPIC: {
                Task task = new Epic();
                addNewEpic((Epic) taskEditor(taskArr, task));
                break;
            }

            case SUBTASK: {
                Task task = new SubTask(Integer.parseInt(taskArr[5]));
                addNewSubtask(Integer.parseInt(taskArr[5]), (SubTask) taskEditor(taskArr, task));
                // на всякий случай, если файл будет правиться вручную и будет допущена ошибка в статусах эпика
                updateEpicStatus(getEpic(Integer.parseInt(taskArr[5])));
                break;
            }
        }
    }

    private Task taskEditor(String[] taskArr, Task task) { // метод детальнее наполняет каждую задачу из файла
        task.setId(Integer.parseInt(taskArr[0]));
        task.setName(taskArr[2]);
        task.setStatus(Status.valueOf(taskArr[3]));
        task.setDescription(taskArr[4]);
        return task;
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

