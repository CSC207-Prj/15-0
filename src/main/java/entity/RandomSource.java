package entity;

/**
 * Abstracts randomness used by domain strategies and other randomized game features.
 *
 * User Story 4 injects this abstraction into WeightedFightSimulator, allowing
 * production code to use Java randomness while tests provide deterministic sequences.
 */
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
