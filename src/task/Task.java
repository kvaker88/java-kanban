package task;

// класс для выполнения действий с обычными задачами
public class Task {

    protected String name; // имя
    protected String description; // описание
    protected int id; // айди
    protected Statuses status;

    String taskPrint = "ID задачи = %s, Имя задачи = %s, Описание задачи = %s, Статус задачи = %s \n";

    // метод для создания задачи, который берёт айди, имя задачи и описание из класса TasksManager
    public void createTask(int id, String name, String description) {
        setName(name);
        setId(id);
        setDescription(description);
        this.status = Statuses.NEW;
    }

    // вводим имя задачи и присваиваем его объекту
    public void setName(String name) {
        this.name = name;
    }

    // вводим описание задачи и присваиваем его объекту
    public void setDescription(String description) {
        this.description = description;
    }

    // вводим статус задачи и присваиваем его объекту
    public void setStatus(int ch) {
        switch (ch) {
            case 1: {
                this.status = Statuses.NEW;
                break;
            }
            case 2: {
                this.status = Statuses.IN_PROGRESS;
                break;
            }
            case 3: {
                this.status = Statuses.DONE;
                break;
            }
            default:
                System.out.println("Такого статуса нет.");
        }
    }

    // присваиваем айди задаче, он не может быть изменён
    public void setId(int id) {
        this.id = id;
    }

    // метод для получения айди задачи
    public int getId() {
        return id;
    }

    // метод для получения описания задачи
    public String getDescription() {
        return description;
    }

    // метод для получения статус задачи
    public Statuses getStatus() {
        return status;
    }

    // метод для получения имени задачи
    public String getName() {
        return name;
    }

    // метод для печати задачи
    public void printTask() {
        System.out.print(String.format(taskPrint, this.id, this.name, this.description, this.status));
    }

    // метод для изменения данных задачи
    public void changeTask(int ch, String newValue, int stat) {
        switch (ch) {
            case (1):
                setName(newValue);
                return;
            case (2):
                setDescription(newValue);
                return;
            case (3):
                setStatus(stat);
                return;
            default:
                System.out.print("Такой команды нет");
        }
    }
}