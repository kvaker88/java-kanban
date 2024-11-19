package com.yandex.app.manager;

import com.yandex.app.task.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// класс с менеджером задач для взаимодействия с задачами разных типов
public class TasksManager {
    static final Map<Integer, Task> taskList = new HashMap<>(); // Мапа для обычных задач
    static final Map<Integer, Epic> epicList = new HashMap<>(); // Мапа для Эпиков
    static final Map<Integer, SubTask> subTaskList = new HashMap<>(); // Мапа для подзадач

    private static int id = 1; // счётчик ID задач

    public static String getTasks() {
        if (taskList.isEmpty()) {
            return "Список задач пуст.";
        } else {
            // метод для получения задач, немного замудрёный, чтобы задачи отображались отдельно.
            StringBuilder sb = new StringBuilder("[");
            for (Task task : taskList.values()) {
                sb.append(task).append(", \n");
            }
            if (sb.length() > 1) {
                sb.setLength(sb.length() - 3);
            }
            sb.append("]");
            return sb.toString();
        }
    }

    public static String getSubtasks() { // метод для получения подзадач
        if (subTaskList.isEmpty()) {
            return "Список подзадач пуст.";
        } else {
            StringBuilder sb = new StringBuilder("[");
            for (Task task : subTaskList.values()) {
                sb.append(task).append(", \n");
            }
            if (sb.length() > 1) {
                sb.setLength(sb.length() - 3);
            }
            sb.append("]");
            return sb.toString();
        }
    }

    public static String getEpics() { // метод для получения эпиков
        if (epicList.isEmpty()) {
            return "Список Эпиков пуст.";
        } else {
            StringBuilder sb = new StringBuilder("[");
            for (Task task : epicList.values()) {
                sb.append(task).append(", \n");
            }
            if (sb.length() > 1) {
                sb.setLength(sb.length() - 3);
            }
            sb.append("]");
            return sb.toString();
        }
    }

    public static List<SubTask> getEpicSubtasks(int epicId) { // метод для получения подзадач эпика по ID
        List<SubTask> subtasksForEpic = new ArrayList<>();

        // Перебираем все подзадачи и добавляем те, которые принадлежат заданному эпик ID
        for (SubTask subTask : subTaskList.values()) {
            if (subTask.epicId == epicId) {
                subtasksForEpic.add(subTask);
            }
        }
        if (subtasksForEpic.isEmpty()) {
            System.out.println("Подзадачи по указанному ID не найдены.");
            return null;
        } else {
            return subtasksForEpic;
        }
    }

    public static void addNewTask(String name, String description) { // метод для добавления задачи
        Task task = new Task(id, name, description);
        taskList.put(id++, task);
    }

    public static void addNewEpic(String name, String description) { // метод для добавления эпика
        Epic task = new Epic(id, name, description);
        epicList.put(id++, task);
    }

    public static void addNewSubtask(int epicId, String name, String description) { // метод для добавления подзадачи
        SubTask task = new SubTask(id, epicId, name, description);
        subTaskList.put(id++, task);
    }

    public static Epic getEpic(int id) { // метод для получения информации по отдельному Эпику по ID
        return epicList.get(id);
    }

    public static Task getTask(int id) { // метод для получения информации по отдельной подзадаче по ID
        return taskList.get(id);
    }

    public static SubTask getSubtask(int id) { // метод для получения информации по отдельной подзадаче по ID
        return subTaskList.get(id);
    }

    // метод для обновления информации в задаче по ID
    public static void updateTask(Task task) { // метод для обновления информации обычных задач
        Task currenTask = taskList.get(task.getId());
        currenTask.setName(task.getName());
        currenTask.setDescription(task.getDescription());

        taskList.put(task.getId(), task);
    }

    // метод для обновления информации в Эпике по ID
    public static void updateEpic(Epic epic) { // метод для обновления информации Эпиков
        Epic currenEpic = epicList.get(epic.getId());
        currenEpic.setName(epic.getName());
        currenEpic.setDescription(epic.getDescription());

        epicList.put(epic.getId(), epic);
    }

    // метод для обновления информации в подзадаче по ID
    public static void updateSubtask(SubTask subTask) { // метод для обновления информации подзадач
        SubTask currenSubTask = subTaskList.get(subTask.getId());
        currenSubTask.setName(subTask.getName());
        currenSubTask.setDescription(subTask.getDescription());
        currenSubTask.setStatus(subTask.getStatus());
        subTaskList.put(subTask.getId(), currenSubTask);
        Epic epic = epicList.get(currenSubTask.getEpicId());
        setStatusSubTask(epic, currenSubTask);
    }

    public static void deleteTask(int id) { // метод для удаления задачи по ID
        taskList.remove(id);
        System.out.println("Задача с ID - " + id + ", удалена.");
    }

    public static void deleteEpic(int id) { // метод для удаления эпика и его подзадач по ID
        List<SubTask> subTasks = getEpicSubtasks(id);
        for (SubTask subTask : subTasks) {
            subTaskList.remove(subTask.getId());
        }
        epicList.remove(id);
        System.out.println("Эпик с ID - " + id + ", и его подзадачи удалены.");
    }

    public static void deleteSubtask(int id) { // метод для удаления подзадачи по ID
        subTaskList.remove(id);
        System.out.println("Подзадача с ID - " + id + ", удалена.");
    }

    public static void deleteTasks() { // метод для удаления всех обычных задач
        taskList.clear();
        System.out.println("Все обычные задачи удалены!");
    }

    public static void deleteSubtasks() { // метод для удаления всех подзадач
        subTaskList.clear();
        System.out.println("Все подзадачи удалены!");
    }

    public static void deleteEpics() { // метод для удаления всех Эпиков и подзадач
        epicList.clear();
        subTaskList.clear();
        System.out.println("Эпики и подзадачи удалены!");
    }

    // метод для проверки и изменения статуса Эпика при изменении статуса подзадач
    private static void setStatusSubTask(Epic epic, SubTask subTask) {
        if (subTask.getStatus() == Status.IN_PROGRESS) {
            epic.setStatus(Status.IN_PROGRESS);
            return;
        }
        if (subTask.getStatus().toString().equalsIgnoreCase("DONE")) {
            for (Task epicSubTask : getEpicSubtasks(epic.getId())) {
                if (!Status.DONE.equals(epicSubTask.getStatus())) {
                    epic.setStatus(Status.IN_PROGRESS);
                    return;
                }
            }
            epic.setStatus(Status.DONE);
        }
    }

}
