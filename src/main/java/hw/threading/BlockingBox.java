package hw.threading;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;

class BlockingBox<ElementType> {
    private volatile ElementType value = null;
    private AtomicBoolean ready = new AtomicBoolean(false);

    public synchronized void set(ElementType value) {
        this.value = value;
        ready.set(true);
        notifyAll();
    }

    public synchronized ElementType waitAndGet() throws InterruptedException {
        while (!ready.get()) {
            wait();
        }
        return value;
    }

    public ElementType get() throws NoSuchElementException {
        if (!ready.get()) {
            throw new NoSuchElementException();
        }
        return value;
    }

}
