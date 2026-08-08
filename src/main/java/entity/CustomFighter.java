package entity;

import java.util.Map;

/** Blank player-built fighter. Drafting and reroll behaviour is added in later stages. */
public final class CustomFighter extends Fighter {
    public CustomFighter(String temporaryName) {
        super(temporaryName, null, new FighterRecord(), Map.of());
    }
}
