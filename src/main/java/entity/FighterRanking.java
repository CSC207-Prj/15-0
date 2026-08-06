package entity;

import java.util.Comparator;

/**
 * The ranking policy for saved fighters. This is a core business rule (it
 * defines what "better" means for two fighters), so it lives in the entity
 * layer where any use case can apply it.
 *
 * <p>Order, from the user story's defined tie-breaker:</p>
 * <ol>
 *     <li>more wins first</li>
 *     <li>fewer losses first</li>
 *     <li>more finishes (KO or submission wins) first</li>
 *     <li>name, alphabetically, so the ordering is always deterministic</li>
 * </ol>
 */
public final class FighterRanking {

    private FighterRanking() {
        // static utility class; not meant to be instantiated
    }

    /**
     * Returns a comparator that sorts fighters from best to worst record
     * using the tie-breaking rules above.
     * @return the roster ranking comparator
     */
    public static Comparator<CustomFighter> byRecord() {
        return Comparator
                .comparingInt((CustomFighter fighter) -> fighter.getRecord().getWins()).reversed()
                .thenComparingInt(fighter -> fighter.getRecord().getLosses())
                .thenComparing(fighter -> fighter.getRecord().getFinishes(), Comparator.reverseOrder())
                .thenComparing(Fighter::getName, String.CASE_INSENSITIVE_ORDER);
    }
}
