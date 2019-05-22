package hw.threading;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class SimpleExecutionManagerTest {

    @Test
    void callback() throws InterruptedException {
        AtomicBoolean callback = new AtomicBoolean(false);
        ExecutionManager executionManager = new SimpleExecutionManager(1);
        executionManager.execute(() -> {
            callback.set(true);
        }, () -> {});
        Thread.sleep(100);
        assert callback.get();
    }
    @Test
    void interrupt() throws InterruptedException {
        AtomicInteger nCallback = new AtomicInteger(0);
        AtomicInteger value = new AtomicInteger(0);
        ExecutionManager executionManager = new SimpleExecutionManager(1);
        ExecutionContext executionContext = executionManager.execute(() -> {
            nCallback.incrementAndGet();
        }, () -> {
            value.incrementAndGet();
        }, () -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) { }
            value.incrementAndGet();
        }, () -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) { }
            value.incrementAndGet();
        });
        while (value.get() == 0);
        executionContext.interrupt();
        assert nCallback.get() == 0;
        Thread.sleep(1000);
        assert value.get() == 2;
    }
}