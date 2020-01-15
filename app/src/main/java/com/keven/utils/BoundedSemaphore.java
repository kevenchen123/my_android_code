package com.keven.utils;

import java.util.concurrent.Semaphore;

public class BoundedSemaphore extends Semaphore {

    final int bound;

    public BoundedSemaphore(int permits) {
        super(permits);
        bound = permits;
    }

    @Override
    public void acquire() throws InterruptedException {
        super.acquire();
    }

    @Override
    public boolean tryAcquire() {
        return super.tryAcquire();
    }

    @Override
    public void release() {
        if (availablePermits() < bound) {
            super.release();
        }
    }

    @Override
    public void acquire(int count) throws InterruptedException {
        super.acquire(count);
    }

    @Override
    public boolean tryAcquire(int count) {
        return super.tryAcquire(count);
    }

    @Override
    public void release(int count) {
        if (availablePermits() < bound) {
            super.release(bound - availablePermits());
        }
    }
}
