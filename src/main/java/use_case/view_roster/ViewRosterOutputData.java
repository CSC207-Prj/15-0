package use_case.view_roster;

import java.util.List;

/**
 * Output data for the View Roster use case: every saved fighter sorted from
 * best to worst record, plus the top three for the highlight panel.
 */
public class ViewRosterOutputData {
    private final List<RosterEntryData> rankedFighters;
    private final List<RosterEntryData> topThree;

    public ViewRosterOutputData(List<RosterEntryData> rankedFighters,
                                List<RosterEntryData> topThree) {
        this.rankedFighters = rankedFighters;
        this.topThree = topThree;
    }

    public List<RosterEntryData> getRankedFighters() {
        return rankedFighters;
    }

    public List<RosterEntryData> getTopThree() {
        return topThree;
    }
}
