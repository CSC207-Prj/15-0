package use_case.browse_fighters;

import entity.Attribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Raw fighter data returned by the Fighter Browser use case.
 */
public class BrowseFightersOutputData {
    private final List<FighterSummary> fighters;
    private final FighterProfile selectedFighter;

    public BrowseFightersOutputData(List<FighterSummary> fighters,
                                    FighterProfile selectedFighter) {
        this.fighters = new ArrayList<>(fighters);
        this.selectedFighter = selectedFighter;
    }

    public List<FighterSummary> getFighters() {
        return Collections.unmodifiableList(fighters);
    }

    public FighterProfile getSelectedFighter() {
        return selectedFighter;
    }

    /**
     * Lightweight list entry used by the catalogue.
     */
    public static class FighterSummary {
        private final String name;
        private final String weightClass;
        private final String era;

        public FighterSummary(String name, String weightClass, String era) {
            this.name = name;
            this.weightClass = weightClass;
            this.era = era;
        }

        public String getName() {
            return name;
        }

        public String getWeightClass() {
            return weightClass;
        }

        public String getEra() {
            return era;
        }
    }

    /**
     * Complete profile for the selected fighter.
     */
    public static class FighterProfile {
        private final String name;
        private final String weightClass;
        private final String era;
        private final String professionalRecord;
        private final int rank;
        private final EnumMap<Attribute, Double> attributes;

        public FighterProfile(String name,
                              String weightClass,
                              String era,
                              String professionalRecord,
                              int rank,
                              Map<Attribute, Double> attributes) {
            this.name = name;
            this.weightClass = weightClass;
            this.era = era;
            this.professionalRecord = professionalRecord;
            this.rank = rank;
            this.attributes = new EnumMap<>(Attribute.class);
            this.attributes.putAll(attributes);
        }

        public String getName() {
            return name;
        }

        public String getWeightClass() {
            return weightClass;
        }

        public String getEra() {
            return era;
        }

        public String getProfessionalRecord() {
            return professionalRecord;
        }

        public int getRank() {
            return rank;
        }

        public Map<Attribute, Double> getAttributes() {
            return Collections.unmodifiableMap(attributes);
        }
    }
}
