package cn.chengzhimeow.mhdftools.thread;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public final class ThreadPool {
    @Getter
    private final String id;
    private final ScheduledExecutorService executor;

    public ThreadPool(int size, @NotNull String id) {
        this.id = id;
        this.executor = new ScheduledThreadPoolExecutor(size, new ThreadFactory() {
            private int index = 0;

            @Override
            public java.lang.Thread newThread(@NotNull Runnable runnable) {
                this.index++;
                if (size == 1) return new java.lang.Thread(runnable, ThreadPool.this.id);
                return new java.lang.Thread(runnable, ThreadPool.this.id + " Pool-" + this.index);
            }
        });
    }

    public ThreadPool(@NotNull String id) {
        this(1, id);
    }

    public void kill() {
        this.executor.shutdown();
    }

    public void execute(@NotNull Runnable task) {
        this.executor.submit(task);
    }

    public void schedule(@NotNull Runnable task, long delay) {
        this.executor.schedule(task, delay, TimeUnit.MILLISECONDS);
    }

    public void schedule(@NotNull Runnable task, long delay, long period) {
        this.executor.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS);
    }
}
