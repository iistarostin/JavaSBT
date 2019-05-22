package hw.threading;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

class FixedThreadPoolTest {

    @Test
    public void manyTasksTest() {
        int[] x = new int[12];
        AtomicInteger ndone = new AtomicInteger(0);
        ThreadPool threadPool = new FixedThreadPool(3);
        for (int i = 0; i < 6; ++i) {
            final int n = i;
            threadPool.execute(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
                ++x[n];
                ndone.incrementAndGet();
            });
        }
        threadPool.start();
        for (int i = 6; i < 12; ++i) {
            final int n = i;
            threadPool.execute(() -> {
                ++x[n];
                ndone.incrementAndGet();
            });
        }
        while (ndone.get() < 12);
        assert Arrays.stream(x).allMatch(z -> z == 1);
    }

}