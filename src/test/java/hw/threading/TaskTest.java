package hw.threading;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    void get() {
        AtomicInteger ncalled = new AtomicInteger(0);
        AtomicInteger ndone = new AtomicInteger(0);
        AtomicBoolean correct = new AtomicBoolean(true);

        Task<Integer> task = new Task<>( () -> {
            ncalled.incrementAndGet();
            Thread.currentThread().sleep(100);
            return 5;
        });

        ThreadPool scalableThreadPool = new ScalableThreadPool(3, 6);
        for (int i = 0; i < 6; i++) {
            scalableThreadPool.execute( () -> {
                try {
                    correct.set(correct.get() && task.get() == 5);
                } catch (TaskCallException e) {
                    correct.set(false);
                } catch (InterruptedException e) {
                    return;
                } finally {
                  ndone.incrementAndGet();
                }
            });
        }
        scalableThreadPool.start();
        while (ndone.get() < 6);
        assert correct.get();
        assert ncalled.get() == 1;
    }

    @Test
    void exceptionHandling() {
        Task<Integer> task = new Task<>(() -> {
            throw new RuntimeException();
        });
        try {
            task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TaskCallException e) {
            return;
        }
        Assert.fail();
    }
}