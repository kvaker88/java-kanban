import com.yandex.app.history.HistoryManager;
import com.yandex.app.manager.Managers;
import com.yandex.app.manager.TasksManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ManagersTest {

    @Test
    void ReturnTaskManager() {
        TasksManager taskManager = Managers.getDefault();
        Assertions.assertNotNull(taskManager, "Метод getDefault() должен возвращать TaskManager.");
    }

    @Test
    void ReturnHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Assertions.assertNotNull(historyManager, "Метод getDefaultHistory() должен возвращать HistoryManager.");
    }
}