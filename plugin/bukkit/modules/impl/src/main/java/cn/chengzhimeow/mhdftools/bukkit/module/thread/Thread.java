package cn.chengzhimeow.mhdftools.bukkit.module.thread;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public abstract class Thread {
    @Getter
    private final String id;
    private final ScheduledExecutorService thread;

    protected Thread(int size, String id) {
        this.id = id;
        this.thread = new ScheduledThreadPoolExecutor(size, new ThreadFactory() {
            private int id = 0;

            @Override
            public java.lang.Thread newThread(@NotNull Runnable r) {
                id++;
                if (size == 1) return new java.lang.Thread(r, Thread.this.id);
                return new java.lang.Thread(r, Thread.this.id + " Pool-" + id);
            }
        });
    }

    protected Thread(String id) {
        this(1, id);
    }

    /**
     * 关闭线程池
     */
    public void kill() {
        this.thread.shutdown();
    }

    /**
     * 执行任务
     *
     * @param task 任务实例
     */
    public void execute(Runnable task) {
        this.thread.submit(task);
    }

    /**
     * 延迟执行任务
     *
     * @param task  任务实例
     * @param delay 延迟时间(单位: 毫秒)
     */
    public void schedule(Runnable task, long delay) {
        this.thread.schedule(task, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * 定时执行任务
     *
     * @param task   任务实例
     * @param delay  延迟时间(单位: 毫秒)
     * @param period 间隔时间(单位: 毫秒)
     */
    public void schedule(Runnable task, long delay, long period) {
        this.thread.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS);
    }
}
