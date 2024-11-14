package task;

// класс для выполнения действий с подзадачами
public class SubTask extends Task {
    Epic epic; // у каждой подзадачи есть свой эпик, к которому он привязан
    String taskPrint = "ID задачи = %s, Эпик задача = %s, Имя задачи = %s, " +
            "Описание задачи = %s, Статус задачи = %s \n";

    // метод для создания подзадачи
    public void createSubTask(int id, Epic newEpic, String name, String description) {
        if (setEpic(newEpic)) { // если у подзадачи выбран неподходящий эпик, то создание подзадачи отменяется
            setName(name);
            setDescription(description);
            setStatusSubTask(1); // задаём дефолтный статус NEW для подзадач
            this.id = id;
        }
    }

    // метод для присваивания эпика
    public boolean setEpic(Task newEpic) {
        if (newEpic.getClass().toString().contains("Epic")) { // проверяем является ли задача Эпиком
            this.epic = (Epic) newEpic; // присваиваем подзадаче эпик
            epic.addSubTask(this); // добавляем подзадачу в список подзадач у Эпика
        } else {
            System.out.println("Выбранная задача не Эпик");
            return false;
        }
        return true;
    }

    // метод для изменения статуса подзадачи
    public void setStatusSubTask(int ch) {
        if (this.epic != null) {
            setStatus(ch);
            if (this.status == Statuses.IN_PROGRESS) {
                epic.status = Statuses.IN_PROGRESS;
            }
            if (this.status.toString().equalsIgnoreCase("DONE")){
                epic.checkStatus();
            }
        } else {
            System.out.println("Эпик не выбран.");
            System.out.println(epic);
        }
    }

    // получаем эпик подзадачи
    public Epic getEpic() {
        return epic;
    }

    // переопределяем метод для печати подзадачи
    @Override
    public void printTask() {
        System.out.printf(taskPrint, this.id, this.epic.getName(),
                this.name, this.description, this.status);
    }

    // переопределяем метод для изменения подзадачи
    @Override
    public void changeTask(int ch, String newValue, int stat) {
        switch (ch) {
            case (1):
                setName(newValue);
                break;
            case (2):
                setDescription(newValue);
                break;
            case (3):
                setStatusSubTask(stat);
                break;
            default:
                System.out.print("Такой команды нет");
        }
    }
}

