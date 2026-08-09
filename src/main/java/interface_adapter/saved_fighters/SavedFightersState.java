package interface_adapter.saved_fighters;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the information displayed by the Saved Fighters screen: the ranked
 * roster, the top-three highlight, the latest loaded fighter and exhibition
 * result, and any message or error to show.
 */
public class SavedFightersState {
    private List<SavedFighterRow> rows = new ArrayList<>();
    private List<SavedFighterRow> topThree = new ArrayList<>();
    private String loadedFighterDetails = "";
    private String exhibitionResult = "";
    private String message = "";
    private String error = "";

    public List<SavedFighterRow> getRows() {
        return new ArrayList<>(rows);
    }

    public void setRows(List<SavedFighterRow> rows) {
        this.rows = new ArrayList<>(rows);
    }

    public List<SavedFighterRow> getTopThree() {
        return new ArrayList<>(topThree);
    }

    public void setTopThree(List<SavedFighterRow> topThree) {
        this.topThree = new ArrayList<>(topThree);
    }

    public String getLoadedFighterDetails() {
        return loadedFighterDetails;
    }

    public void setLoadedFighterDetails(String loadedFighterDetails) {
        this.loadedFighterDetails = loadedFighterDetails == null ? "" : loadedFighterDetails;
    }

    public String getExhibitionResult() {
        return exhibitionResult;
    }

    public void setExhibitionResult(String exhibitionResult) {
        this.exhibitionResult = exhibitionResult == null ? "" : exhibitionResult;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message == null ? "" : message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error == null ? "" : error;
    }
}
