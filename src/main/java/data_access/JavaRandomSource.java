package data_access;

import entity.RandomSource;

import java.util.concurrent.ThreadLocalRandom;

/** Production RandomSource implementation backed by Java's ThreadLocalRandom. */
public final class JavaRandomSource implements RandomSource {
    @Override
    public double nextDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    @Override
    public int nextInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }
}
