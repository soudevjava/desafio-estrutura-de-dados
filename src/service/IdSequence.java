package service;

import java.util.concurrent.atomic.AtomicInteger;

public final class IdSequence {
    private static final IdSequence INSTANCE = new IdSequence();
    private final AtomicInteger counter = new AtomicInteger(0);

    private IdSequence() {}

    public static IdSequence getInstance() { return INSTANCE; }

    public int nextId() { return counter.incrementAndGet(); }

}
