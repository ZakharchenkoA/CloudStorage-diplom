package ru.netology.cloudstorage.generator;

import java.util.concurrent.atomic.AtomicInteger;

public class GeneratorId {

    private static final Integer INITIAL_VALUE = 1;
    private final AtomicInteger nextId = new AtomicInteger(INITIAL_VALUE);

    private GeneratorId() {
    }

    private static class GeneratorIdHolder {
        public static final GeneratorId generatorId = new GeneratorId();
    }

    public static GeneratorId getGeneratorId() {
        return GeneratorIdHolder.generatorId;
    }

    public Integer getId() {
        return nextId.getAndIncrement();
    }
}