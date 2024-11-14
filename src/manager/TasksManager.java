package manager;

import task.Epic;
import task.SubTask;
import task.Task;

import java.util.List;
import java.util.ArrayList;

// класс с менеджером задач для взаимодействия с задачами разных типов
public class TasksManager {
    private final List<Task> tasks = new ArrayList<>();
    int id = 1; // счётчик ID задач

    // метод для добавления задачи и Эпиков в список
    public void addTask(String taskType, String name, String description) {
        switch (taskType) {
            case ("Task"): {
                Task task = new Task();
                task.createTask(id, name, description);
                this.tasks.add(task);
                id++;
                break;
            }
            case ("Epic"): {
                Epic task = new Epic();
                task.createEpic(id, name, description);
                this.tasks.add(task);
                id++;
                break;
            }
            default:
                System.out.print("Такого типа задач нет.");
        }
    }

    // метод для создания подзадач
    public void addSubTask(int epicId, String name, String description) {
        SubTask task = new SubTask();
        task.createSubTask(id, checkEpicFromList(epicId), name, description);
        this.tasks.add(task);
        id++;
    }

    // метод для удаления задачи по ID
    public void removeTask(int id) {
        if (checkID(id)) {
            Task task; // создаём объект задачу
            task = getByID(id); // получаем задачу по айди
            removeSubTasks(tasks.indexOf(task), id); // Если эпик - удаляем подзадачи из списка Эпика
            removeSubTaskFromEpic(tasks.indexOf(task), id, task); // Если подзадача - удаляем подзадачу из списка Эпика
            this.tasks.remove(tasks.indexOf(task)); // удаляем саму задачу из списка
            System.out.println("Задача удалена");
        }

    }

    // метод для удаления подзадач у эпика
    public void removeSubTasks(int index, int id) {
        if (tasks.get(index).getClass().toString().contains("Epic")) { // проверяем является ли задача эпиком
            Epic epic = (Epic) getByID(id); // получаем эпик задачу
            List<Task> subTasks = epic.getSubTasks(); // получаем список подзадач
            for (Task subTask : subTasks) {
                this.tasks.remove(tasks.indexOf(subTask)); // удаляем подзадачи из списка задач
            }
            System.out.println("Подзадачи удалены");
        }
    }

    // метод для удаления подзадачи из списка Эпика
    public void removeSubTaskFromEpic(int index, int id, Task task) {
        if (tasks.get(index).getClass().toString().contains("SubTask")) { // проверяем является ли задача подзадачей
            SubTask subTask = (SubTask) getByID(id); // получаем подзадачу
            List<Task> subTasks = subTask.getEpic().getSubTasks(); // получаем список задач из эпика
            subTasks.remove(subTasks.indexOf(task)); // удаляем подзадачу из списка задач эпика
            System.out.println("Подзадачи удалены");
        }
    }

    // метод для проверки является ли задача эпиком или подзадачей
    public Task checkEpicOrSub(Task task) {
        if (task.getClass().toString().contains("Epic")) {
            return task;
        }
        if (task.getClass().toString().contains("SubTask")) {
            return task;
        }
        return task;
    }

    // метод для печати всех задач
    public void printTasks() {
        if (tasks.size() == 0) {
            System.out.println("Задач нет");
            return;
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        for (Task task : tasks) {
            task = checkEpicOrSub(task);
            task.printTask();
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }
    }


    // метод для печати определённой задачи по ID
    public void printTaskByID(int id) {
        Task task;
        task = getByID(id);

        if (checkID(id)) {
            task = checkEpicOrSub(task);
            task.printTask();
        }
    }


    // метод для изменения задачи по ID
    public void changeTaskByID(int id, int ch, String newValue, int stat) {
        // id - выбор задачи, ch (choice) - выбор действия с задачей
        // newValue - новое значение для имени или описания, stat - для изменения статуса
        if (!checkID(id)) {
        } else {
            Task task;
            task = getByID(id);
            task.changeTask(ch, newValue, stat);
        }
    }


    // метод для печати подзадач у Эпика
    public void getSubTasksByID(int id) {
        Task task;
        task = getByID(id);

        if (checkID(id)) {
            if (task.getClass().toString().contains("Epic")) {
                Epic epic = (Epic) task;
                epic.printSubTasks();
            } else {
                System.out.println("Выбранная задача не Эпик");
            }
        }
    }

    // метод для проверки существует ли задача в списке по ID
    public boolean checkID(int id) {
        for (Task task : getTasks()) {
            if (task.getId() == id) {
                return true;
            }
        }
        System.out.println("Задачи с таким ID нет");
        return false;
    }

    // метод для получения задачи по ID
    public Task getByID(int id) {
        for (Task task : getTasks())
            if (task.getId() == id) {
                return task;
            }
        Task task = null;
        return task;
    }

    public Epic checkEpicFromList(int epicId) {
        if (!checkID(epicId)) { // проверяем существует ли эпик
            System.out.println("Такого эпика не существует");
            return null;
        }
        Task newEpic;
        newEpic = getByID(epicId); // создаём новый эпик по найденному айди
        return (Epic) newEpic;
    }

    // метод для удаления всех задач
    public void removeAllTasks() {
        this.tasks.removeAll(tasks);
        System.out.println("Задачи удалены");
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
