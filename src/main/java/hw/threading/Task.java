package hw.threading;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

public class Task<T> {
    private final Callable<? extends T> taskToDo;
    private volatile TaskCallException exception = null;
    private BlockingBox<T> value = new BlockingBox<>();
    AtomicBoolean isStarted = new AtomicBoolean();
    AtomicBoolean isFinished = new AtomicBoolean();

    public Task(Callable<? extends T> taskToDo) {
        this.taskToDo = taskToDo;
    }

    public boolean isRunning() { return isStarted.get() && !isFinished.get(); }
    public boolean isDone() { return isFinished.get(); }

    public T get() throws InterruptedException {
        if (isFinished.get()) {
            //value ready, return it
            if (exception != null) {
                throw exception;
            }
            return value.get();
        }
        if (isStarted.compareAndSet(false, true)) {
            //This thread will do the job
            try {
                value.set(taskToDo.call());
                return value.get();
            } catch (Throwable e) {
                exception = new TaskCallException(e);
                value.set(null);
                throw exception;
            } finally {
                isFinished.set(true);
            }
        } else {
            T result = value.waitAndGet();
            if (exception != null) {
                throw exception;
            }
            return result;
        }
    }
}
