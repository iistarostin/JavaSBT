package hw.threading;

public interface ExecutionManager {
    ExecutionContext execute (Runnable callback, Runnable... tasks);
}
