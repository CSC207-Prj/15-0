package use_case.view_roster;

import java.util.ArrayList;
import java.util.List;

import entity.CustomFighter;
import entity.FighterRanking;

/**
 * Interactor for the View Roster use case: loads every saved fighter, sorts
 * them with the roster ranking policy, and presents the ranked list along
 * with the top three.
 */
public class ViewRosterInteractor implements ViewRosterInputBoundary {
    private static final int TOP_COUNT = 3;
    private static final String WEIGHT_CLASS_UNASSIGNED = "TBD";

    private final ViewRosterDataAccessInterface rosterDataAccess;
    private final ViewRosterOutputBoundary presenter;

    public ViewRosterInteractor(ViewRosterDataAccessInterface rosterDataAccess,
                                ViewRosterOutputBoundary presenter) {
        this.rosterDataAccess = rosterDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        final List<CustomFighter> fighters = new ArrayList<>(rosterDataAccess.getAllFighters());
        fighters.sort(FighterRanking.byRecord());

        final List<RosterEntryData> ranked = new ArrayList<>();
        for (CustomFighter fighter : fighters) {
            ranked.add(toEntry(fighter));
        }

        final int topEnd = Math.min(TOP_COUNT, ranked.size());
        final List<RosterEntryData> topThree = new ArrayList<>(ranked.subList(0, topEnd));

        presenter.prepareSuccessView(new ViewRosterOutputData(ranked, topThree));
    }

    private static RosterEntryData toEntry(CustomFighter fighter) {
        final String weightClassName;
        if (fighter.getWeightClass() == null) {
            weightClassName = WEIGHT_CLASS_UNASSIGNED;
        }
        else {
            weightClassName = fighter.getWeightClass().getDisplayName();
        }
        return new RosterEntryData(
                fighter.getName(),
                weightClassName,
                fighter.getRecord().getWins(),
                fighter.getRecord().getLosses(),
                fighter.getRecord().getFinishes(),
                fighter.getRecord().getWins() + "-" + fighter.getRecord().getLosses());
    }
}
