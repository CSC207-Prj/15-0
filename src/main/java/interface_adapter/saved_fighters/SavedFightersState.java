package interface_adapter.saved_fighters;

import java.util.ArrayList;
import java.util.List;

import entity.CustomFighter;

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
    private CustomFighter duplicatePending;

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

    /**
     * Sets the loaded fighter summary line; null clears it.
     * @param loadedFighterDetails the summary text, or null
     */
    public void setLoadedFighterDetails(String loadedFighterDetails) {
        if (loadedFighterDetails == null) {
            this.loadedFighterDetails = "";
        }
        else {
            this.loadedFighterDetails = loadedFighterDetails;
        }
    }

    public String getExhibitionResult() {
        return exhibitionResult;
    }

    /**
     * Sets the latest exhibition result line; null clears it.
     * @param exhibitionResult the result text, or null
     */
    public void setExhibitionResult(String exhibitionResult) {
        if (exhibitionResult == null) {
            this.exhibitionResult = "";
        }
        else {
            this.exhibitionResult = exhibitionResult;
        }
    }

    public String getMessage() {
        return message;
    }

    /**
     * Sets the success message shown to the user; null clears it.
     * @param message the message text, or null
     */
    public void setMessage(String message) {
        if (message == null) {
            this.message = "";
        }
        else {
            this.message = message;
        }
    }

    public String getError() {
        return error;
    }

    /**
     * Sets the error message shown to the user; null clears it.
     * @param error the error text, or null
     */
    public void setError(String error) {
        if (error == null) {
            this.error = "";
        }
        else {
            this.error = error;
        }
    }

    /**
     * Returns the fighter whose save failed because its name is taken, so
     * the view can offer a rename and retry. Null when nothing is pending.
     * @return the unsaved fighter, or null
     */
    public CustomFighter getDuplicatePending() {
        return duplicatePending;
    }

    public void setDuplicatePending(CustomFighter duplicatePending) {
        this.duplicatePending = duplicatePending;
    }
}
