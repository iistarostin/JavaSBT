package hw.threading;

public interface ThreadPool {
    void start();

    void execute(Runnable runnable);
}
