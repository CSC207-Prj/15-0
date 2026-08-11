package entity;

/** Provides random values for fighter selection and fight simulation. */
public interface RandomSource {
    /**
     * Returns the next pseudo-random double used by a randomized strategy.
     *
     * @return value in the implementation-defined random range
     */
    double nextDouble();

    /**
     * Returns a pseudo-random integer below the supplied exclusive bound.
     *
     * @param bound exclusive upper bound
     * @return pseudo-random integer less than bound
     */
    int nextInt(int bound);
}
