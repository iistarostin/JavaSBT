package hw.threading;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.*;

class ScalableThreadPoolTest {

    @Test
    void scalabilityTest() throws InterruptedException {
        AtomicInteger value = new AtomicInteger(0);
        Lock lock1 = new ReentrantLock(), lock2 = new ReentrantLock();
        lock1.lock();
        lock2.lock();
        AtomicInteger ndone = new AtomicInteger(0);
        ThreadPool threadPool = new ScalableThreadPool(3, 6);
        for (int i = 0; i < 4; ++i) {
            threadPool.execute(() -> {
                lock1.lock();
                value.incrementAndGet();
                ndone.incrementAndGet();
                lock1.unlock();
            });
        }
        threadPool.start();
        assert ((ScalableThreadPool) threadPool).getCurrentThreadCount() == 3;
        for (int i = 4; i < 10; ++i) {
            final int n = i;
            threadPool.execute(() -> {
                lock2.lock();
                value.incrementAndGet();
                ndone.incrementAndGet();
                lock2.unlock();
            });
        }
        assert ((ScalableThreadPool) threadPool).getCurrentThreadCount() == 6;
        lock2.unlock();
        while (ndone.get() < 6);
        Thread.currentThread().sleep(1000);
        assert ((ScalableThreadPool) threadPool).getCurrentThreadCount() == 4;
        assert value.get() == 6;
        lock1.unlock();
        while (ndone.get() < 10);
        Thread.currentThread().sleep(1000);
        assert value.get() == 10;

    }
}