package hw.threading;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleExecutionManager implements ExecutionManager {

    private class HackedThreadPool extends FixedThreadPool {
        public HackedThreadPool(int numberOfThreads) {
            super(numberOfThreads);
        }
        public BlockingQueue<Runnable> getTaskQueue() {
            return super.tasks;
        }
    }

    private final int numberOfThreads;
    public SimpleExecutionManager(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    @Override
    public ExecutionContext execute(Runnable callback, Runnable... tasks) {
        HackedThreadPool threadPool = new HackedThreadPool(numberOfThreads);
        AtomicInteger completedCount = new AtomicInteger(0);
        AtomicInteger failedCount = new AtomicInteger(0);
        AtomicInteger interruptedCount = new AtomicInteger(0);

        final int numberOfTasks = tasks.length;
        threadPool.start();
        for (Runnable task : tasks) {
            threadPool.execute(() -> {
                try {
                    task.run();
                    completedCount.incrementAndGet();
                } catch (Exception e) {
                    failedCount.incrementAndGet();
                } finally {
                    if (completedCount.get() + failedCount.get() == numberOfTasks) {
                        callback.run();
                    }
                }
            });
        }

        return new ExecutionContext() {
            @Override
            public int getCompletedTaskCount() {
                return completedCount.get();
            }

            @Override
            public int getFailedTaskCount() {
                return failedCount.get();
            }

            @Override
            public int getInterruptedTaskCount() {
                return interruptedCount.get();
            }

            @Override
            public void interrupt() {
                Collection<Runnable> interruptedTasks = new HashSet<>();
                threadPool.getTaskQueue().drainTo(interruptedTasks);
                interruptedCount.addAndGet(interruptedTasks.size());
            }

            @Override
            public boolean isFinished() {
                return completedCount.get() + interruptedCount.get() == numberOfTasks;
            }
        };
    }
}
