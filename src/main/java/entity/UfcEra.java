package entity;


public enum UfcEra {
    EARLY_UFC("Early UFC (1993-2004)", 1993, 2004),
    GROWTH_ERA("Growth Era (2005-2013)", 2005, 2013),
    MODERN_UFC("Modern UFC (2014-2020)", 2014, 2020),
    CURRENT_UFC("Current UFC (2021-Present)", 2021, Integer.MAX_VALUE);

    private final String displayName;
    private final int startYear;
    private final int endYear;

    UfcEra(String displayName, int startYear, int endYear) {
        this.displayName = displayName;
        this.startYear = startYear;
        this.endYear = endYear;
    }

    public int getStartYear() {
        return startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public boolean containsYear(int year) {
        return year >= startYear && year <= endYear;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
