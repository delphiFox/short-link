package scheduler;

import data.Store;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DeleteLinkScheduler {
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    Runnable task = Store::deleteLinkByInterval;

    private DeleteLinkScheduler() {
        executor.scheduleAtFixedRate(task, 0, 10, TimeUnit.SECONDS);

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
