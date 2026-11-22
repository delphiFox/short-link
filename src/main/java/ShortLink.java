import exception.OperationException;
import process.Strategy;
import scheduler.DeleteLinkScheduler;

import java.util.Scanner;

import static data.Store.initLocalStore;
import static data.Store.initParams;

public class ShortLink {
    public static void main(String[] args) {
        System.out.println("[ Приложение позволяет сократить URL, получить уникальную короткую ссылку и управлять её параметрами ]");
        System.out.println("[ При перезапуске приложение сохранит свои данные ]");
        System.out.println("[ Для завершения работы введите 'q' ]\n");

        initParams();
        initLocalStore();
        var deletingScheduler = DeleteLinkScheduler.start();

        System.out.println("*** Введите URL для его сокращения с числом переходов через пробел,\n*** или UUID (логин) для управления своими ссылками и нажмите Enter");

        try (var sc = new Scanner(System.in)) {
            String message = sc.nextLine();
            if ("q".equals(message))
                return;
            try {
                Strategy.start(message).runAction();
            } catch (OperationException e) {
                System.err.println(e.getLocalizedMessage());
            }
        } finally {
            deletingScheduler.stop();
        }
    }
}
