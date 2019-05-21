package hw.threading;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class FixedThreadPool implements ThreadPool {

    public final int numberOfThreads;
    protected AtomicBoolean started = new AtomicBoolean(false);
    protected BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>();


    public FixedThreadPool(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    @Override
    public void start() {
        if (started.compareAndSet(false, true)) { //Create threads exactly once
            for (int i = 0; i < numberOfThreads; ++i) {
                new Thread(() -> {
                    while (true) {
                        try {
                            tasks.take().run();
                        } catch (InterruptedException e) {
                            return; //stop the execution and terminate the thread
                        }
                    }
                }).start();
            }
        }
    }

    @Override
    public void execute(Runnable task) {
        try {
            tasks.put(task);
        } catch (InterruptedException e) {
            return; //stop the execution and terminate the thread
        }
    }
}
