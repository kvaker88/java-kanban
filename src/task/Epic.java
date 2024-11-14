package task;

import java.util.ArrayList;
import java.util.List;

// класс для выполнения действий с Эпиками
public class Epic extends Task {
    private final List<Task> subTasks = new ArrayList<>(); // создаём список подзадач
    String taskPrint = "ID задачи = %s, Имя задачи = %s, Описание задачи = %s, Статус задачи = %s, \nПодзадачи:  ";

    // метод для создания Эпика
    public void createEpic(int id, String name, String description) {
        setName(name); // присваиваем имя
        setDescription(description); // присваиваем описание
        this.status = Statuses.NEW; // Статус изначально всегда новый, меняется от подзадач
        this.id = id; // присваиваем айди
    }

    // метод для получения подзадач
    public List<Task> getSubTasks() {
        return subTasks;
    }

    // переписываем метод печати задачи под Эпик
    @Override
    public void printTask() {
        System.out.println(String.format(taskPrint, this.id, this.name, this.description, this.status));
        printSubTasks();
    }

    // метод для печати подзадач Эпика
    public void printSubTasks() {
        if (subTasks.isEmpty()) {
            System.out.println("Подзадач нет");
            return;
        }
        String subTaskPrint = "ID задачи = %s, Имя задачи = %s \n";
        for (Task task : subTasks) {
            System.out.print(String.format(subTaskPrint, task.getId(), task.getName()));
        }
    }

    // метод для проверки подзадач, чтобы сменить статус эпика
    public void checkStatus() {
        for (Task subTask : subTasks) {
            if (!subTask.status.toString().equals("Done")) { // если все подзадачи не были завершены, эпик не завершён
                this.status = Statuses.IN_PROGRESS;
                return;
            }
        }
        this.status = Statuses.DONE;
    }

    // метод для добавления подзадач
    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
    }

    // изменяем метод для "смены" статуса эпика
    @Override
    public void setStatus(int ch) {
        System.out.println("Для смены статуса Эпика необходимо сменить статусы у подзадач");
    }
}