package hw.threading;

import java.util.concurrent.atomic.AtomicInteger;

public class ScalableThreadPool extends FixedThreadPool {

    public final int maxExtraThreads;
    private AtomicInteger extraThreadsCount = new AtomicInteger(0);

    public ScalableThreadPool(int minThreads, int maxThreads) {
        super(minThreads);
        this.maxExtraThreads = maxThreads - minThreads;
    }

    public int getCurrentThreadCount() { return started.get() ? super.numberOfThreads + extraThreadsCount.get() : 0; }

    @Override
    public void execute(Runnable task) {
        super.execute(task);
        if (started.get() && tasks.size() > 0) {
            attemptCreateExtraThread();
        }
    }

    private void attemptCreateExtraThread() {
        int threadCountLocal;
        //TATAS synchronization
        while ((threadCountLocal = extraThreadsCount.get()) < maxExtraThreads) {
            if (extraThreadsCount.compareAndSet(threadCountLocal, threadCountLocal + 1)) {
                new Thread(() -> {
                    Runnable nextTask;
                    while ((nextTask = tasks.poll()) != null) {
                        nextTask.run();
                    }
                    extraThreadsCount.decrementAndGet();
                }).start();
                break;
            }
        }
    }
}
