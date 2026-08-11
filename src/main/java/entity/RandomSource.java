package entity;

/** Provides random values for fighter selection and fight simulation. */
public interface RandomSource {
    double nextDouble();

    int nextInt(int bound);
}
