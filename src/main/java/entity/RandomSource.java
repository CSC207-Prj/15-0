package entity;

/** Shared abstraction over randomness for later wheels and simulation strategies. */
public interface RandomSource {
    double nextDouble();

    int nextInt(int bound);
}
