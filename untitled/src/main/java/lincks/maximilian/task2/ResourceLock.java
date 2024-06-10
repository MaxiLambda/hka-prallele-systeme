package lincks.maximilian.task2;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.locks.Lock;

public class ResourceLock implements Closeable {

    private final Lock lock;

    public ResourceLock(Lock lock) {
        this.lock = lock;
        lock.lock();
    }

    @Override
    public void close() {
        lock.unlock();
    }
}
