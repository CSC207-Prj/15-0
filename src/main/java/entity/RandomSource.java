package entity;

/** Provides random values for fighter selection and fight simulation. */
public interface RandomSource {
    /**
<<<<<<< Updated upstream
     * Returns the next pseudo-random double used by a randomized strategy.
     *
     * @return value in the implementation-defined random range
=======
     * Produces a pseudorandom fractional value.
     *
     * @return a value from zero, inclusive, to one, exclusive
>>>>>>> Stashed changes
     */
    double nextDouble();

    /**
<<<<<<< Updated upstream
     * Returns a pseudo-random integer below the supplied exclusive bound.
     *
     * @param bound exclusive upper bound
     * @return pseudo-random integer less than bound
=======
     * Produces a pseudorandom integer below the supplied bound.
     *
     * @param bound exclusive upper bound
     * @return a value from zero, inclusive, to {@code bound}, exclusive
>>>>>>> Stashed changes
     */
    int nextInt(int bound);
}
