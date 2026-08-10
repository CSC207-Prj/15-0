package interface_adapter.fighter_browser;

import entity.Attribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Display-ready state for the Fighter Browser.
 */
public class FighterBrowserState {
    private final List<FighterBrowserRow> rows = new ArrayList<>();
    private final EnumMap<Attribute, Integer> attributes =
            new EnumMap<>(Attribute.class);

    private String selectedName = "No fighter selected";
    private String selectedDetails = "";
    private String rankText = "";
    private String resultText = "";
    private String errorMessage = "";

    public List<FighterBrowserRow> getRows() {
        return Collections.unmodifiableList(rows);
    }

    public void setRows(List<FighterBrowserRow> rows) {
        this.rows.clear();
        this.rows.addAll(rows);
    }

    public Map<Attribute, Integer> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public void setAttributes(Map<Attribute, Integer> attributes) {
        this.attributes.clear();
        this.attributes.putAll(attributes);
    }

    public String getSelectedName() {
        return selectedName;
    }

    public void setSelectedName(String selectedName) {
        this.selectedName = selectedName;
    }

    public String getSelectedDetails() {
        return selectedDetails;
    }

    public void setSelectedDetails(String selectedDetails) {
        this.selectedDetails = selectedDetails;
    }

    public String getRankText() {
        return rankText;
    }

    public void setRankText(String rankText) {
        this.rankText = rankText;
    }

    public String getResultText() {
        return resultText;
    }

    public void setResultText(String resultText) {
        this.resultText = resultText;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
