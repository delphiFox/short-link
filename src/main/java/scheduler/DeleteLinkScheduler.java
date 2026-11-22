package scheduler;

import data.Store;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Планировщик каждую минуту проверяет наличие ссылок, подлежащих удалению,
 * и удаляет их
 */

public class DeleteLinkScheduler {
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    Runnable task = Store::deleteLinkByInterval;

    private DeleteLinkScheduler() {
        executor.scheduleAtFixedRate(task, 0, 1, TimeUnit.MINUTES);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executor.shutdown();
        }));
    }

    public static DeleteLinkScheduler start() {
        return new DeleteLinkScheduler();
    }

    public void stop() {
        executor.shutdown();
    }
}
