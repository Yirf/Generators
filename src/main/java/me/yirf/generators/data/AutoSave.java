package me.yirf.generators.data;

import java.util.Set;
import java.util.concurrent.*;

public class AutoSave<K1, V1, K2, V2> {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final long periodMinutes;

    private final Cache<K1, V1> cache1;
    private final Repository<K1, V1> repo1;

    private final Cache<K2, V2> cache2;
    private final Repository<K2, V2> repo2;

    public AutoSave(long periodMinutes,
                    Cache<K1, V1> cache1, Repository<K1, V1> repo1,
                    Cache<K2, V2> cache2, Repository<K2, V2> repo2) {
        this.periodMinutes = periodMinutes;
        this.cache1 = cache1;
        this.repo1 = repo1;
        this.cache2 = cache2;
        this.repo2 = repo2;
    }

    public void start() {
        scheduler.scheduleAtFixedRate(() -> {
            CompletableFuture.runAsync(() -> {
                for (K1 key : cache1.keys()) {
                    V1 value = cache1.get(key);
                    if (value != null) {
                        repo1.put(key, value);
                    }
                }
            });

            CompletableFuture.runAsync(() -> {
                for (K2 key : cache2.keys()) {
                    V2 value = cache2.get(key);
                    if (value != null) {
                        repo2.put(key, value);
                    }
                }
            });

        }, 0, periodMinutes, TimeUnit.MINUTES);
    }

    public void stop() {
        scheduler.shutdown();
    }
}