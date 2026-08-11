package use_case.confirm;

import entity.CustomFighter;
import entity.Division;
import entity.GameRun;
import entity.GameSettings;
import entity.WeightClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Handles fighter confirmation and creates the corresponding gauntlet run.
 */
public class ConfirmInteractor implements ConfirmInputBoundary {
    private static final int ATTRIBUTE_COUNT = 6;
    private static final int ATTRIBUTE_MIN = 0;
    private static final int ATTRIBUTE_MAX = 100;
    private static final double TEN_PERCENT = 0.10;
    private static final double FIFTEEN_PERCENT = 0.15;
    private static final double TWENTY_PERCENT = 0.20;
    private static final double TWENTY_FIVE_PERCENT = 0.25;
    private static final double THIRTY_PERCENT = 0.30;

    private static final List<String> weightClasses = new ArrayList<>();

    static {
        Collections.addAll(
                weightClasses,
                "Flyweight",
                "Bantamweight",
                "Featherweight",
                "Lightweight",
                "Welterweight",
                "Middleweight",
                "Light Heavyweight",
                "Heavyweight"
        );
    }

    /**
     * Weight order for attribute: Striking, Defence, Takedowns, Height, Reach, Cardio.
     */
    private static final List<Double> FAST_WEIGHTS = new ArrayList<>();
    private static final List<Double> BALANCED_WEIGHTS = new ArrayList<>();
    private static final List<Double> WELTERWEIGHT_WEIGHTS = new ArrayList<>();
    private static final List<Double> MIDDLEWEIGHT_WEIGHTS = new ArrayList<>();
    private static final List<Double> HEAVY_WEIGHTS = new ArrayList<>();

    static {
        Collections.addAll(FAST_WEIGHTS, TWENTY_PERCENT, TWENTY_PERCENT,
                FIFTEEN_PERCENT, TEN_PERCENT, TEN_PERCENT, TWENTY_FIVE_PERCENT);
        Collections.addAll(BALANCED_WEIGHTS, TWENTY_PERCENT, TWENTY_PERCENT,
                TWENTY_PERCENT, TEN_PERCENT, TEN_PERCENT, TWENTY_PERCENT);
        Collections.addAll(
                WELTERWEIGHT_WEIGHTS, TWENTY_FIVE_PERCENT, TWENTY_PERCENT,
                TWENTY_PERCENT, TEN_PERCENT, TEN_PERCENT, FIFTEEN_PERCENT);
        Collections.addAll(
                MIDDLEWEIGHT_WEIGHTS, TWENTY_FIVE_PERCENT, TWENTY_PERCENT,
                FIFTEEN_PERCENT, FIFTEEN_PERCENT, FIFTEEN_PERCENT, TEN_PERCENT);
        Collections.addAll(HEAVY_WEIGHTS, THIRTY_PERCENT, TWENTY_PERCENT,
                TEN_PERCENT, FIFTEEN_PERCENT, FIFTEEN_PERCENT, TEN_PERCENT);
    }

    private final ConfirmOutputBoundary outputboundary;
    private final ConfirmRunDataAccessInterface runDataAccess;
    private Random random = new Random();

    /**
     * Creates an interactor that confirms fighters without starting a gauntlet run.
     *
     * @param outputboundary the presenter that receives confirmation results
     */
    public ConfirmInteractor(ConfirmOutputBoundary outputboundary) {
        this(outputboundary, null);
    }

    /**
     * Creates an interactor that confirms fighters and starts gauntlet runs.
     *
     * @param outputboundary the presenter that receives confirmation results
     * @param runDataAccess access to the data required to create a gauntlet run
     */
    public ConfirmInteractor(ConfirmOutputBoundary outputboundary,
                             ConfirmRunDataAccessInterface runDataAccess) {
        this.outputboundary = outputboundary;
        this.runDataAccess = runDataAccess;
        this.random = new Random();
    }

    /**
     * Checks that all six fighter attributes contain values in the valid range.
     *
     * @param inputData the fighter data to validate
     * @return true when every attribute is valid; otherwise, false
     */
    private boolean validAttributes(ConfirmInputData inputData) {
        if (inputData == null
                || inputData.getAttributePoints() == null
                || inputData.getAttributePoints().size() != ATTRIBUTE_COUNT) {
            outputboundary.prepareFailureView("All 6 values must be assigned");
            return false;
        }

        for (String attribute : inputData.getAttributePoints()) {
            if (attribute == null
                    || attribute.trim().isEmpty()
                    || "TBD".equalsIgnoreCase(attribute)) {
                outputboundary.prepareFailureView("All 6 values must be assigned");
                return false;
            }

            try {
                final int value = Integer.parseInt(attribute.trim());
                if (value < ATTRIBUTE_MIN || value > ATTRIBUTE_MAX) {
                    outputboundary.prepareFailureView(
                            "Attributes must be between 0 and 100"
                    );
                    return false;
                }
            }
            catch (NumberFormatException exception) {
                outputboundary.prepareFailureView(
                        "Attribute values must be numbers"
                );
                return false;
            }
        }

        return true;
    }

    /**
     * Calculates a fighter's overall rating using its weight-class weights.
     *
     * @param attributePoints the fighter's six attribute values
     * @param weightClass the fighter's weight class
     * @return the rounded weighted overall rating
     */
    private int calculateOverall(List<String> attributePoints,
                                 String weightClass) {
        final List<Double> weights = getWeights(weightClass);
        double totalWeight = 0;

        for (int index = 0; index < ATTRIBUTE_COUNT; index++) {
            final double attributeValue =
                    Double.parseDouble(attributePoints.get(index));
            totalWeight += attributeValue * weights.get(index);
        }

        return (int) Math.round(totalWeight);
    }

    /**
     * Gets the attribute weights for a weight class.
     *
     * @param weightClass the display name of the weight class
     * @return the attribute weights for the weight class
     * @throws IllegalArgumentException when the weight class is not supported
     */
    private List<Double> getWeights(String weightClass) {
        switch (weightClass) {
            case "Flyweight":
            case "Bantamweight":
                return FAST_WEIGHTS;
            case "Featherweight":
            case "Lightweight":
                return BALANCED_WEIGHTS;
            case "Welterweight":
                return WELTERWEIGHT_WEIGHTS;
            case "Middleweight":
                return MIDDLEWEIGHT_WEIGHTS;
            case "Heavyweight":
            case "Light Heavyweight":
                return HEAVY_WEIGHTS;
            default:
                throw new IllegalArgumentException("Invalid weight class");
        }
    }

    /**
     * Randomly assigns a weight class and calculates the fighter's overall rating.
     *
     * @param inputData the fighter data to use for the spin
     */
    @Override
    public void spin(ConfirmInputData inputData) {
        if (!validAttributes(inputData)) {
            return;
        }

        final String weightClass =
                weightClasses.get(random.nextInt(weightClasses.size()));
        final int overall =
                calculateOverall(inputData.getAttributePoints(), weightClass);

        final ConfirmOutputData outputData = new ConfirmOutputData(
                inputData.getFighterName(),
                inputData.getAttributePoints(),
                weightClass,
                overall
        );

        outputboundary.prepareSpinSuccessView(outputData);
    }

    /**
     * Confirms valid fighter data and starts a gauntlet run when configured.
     *
     * @param inputData the fighter data to confirm
     */
    @Override
    public void confirm(ConfirmInputData inputData) {
        if (inputData == null || inputData.getFighterName() == null
                || inputData.getFighterName().isEmpty() ||
                inputData.getAttributePoints().isEmpty()) {
            outputboundary.prepareFailureView(
                    "Fighter name cannot be empty"
            );
            return;
        }

        if (!validAttributes(inputData)) {
            return;
        }

        if (inputData.getWeightClass() == null
                || !weightClasses.contains(inputData.getWeightClass())) {
            outputboundary.prepareFailureView(
                    "Spin weight class before confirming"
            );
            return;
        }

        if (runDataAccess != null) {
            createGauntletRun(inputData);
        }

        final ConfirmOutputData outputData = new ConfirmOutputData(
                inputData.getFighterName(),
                inputData.getAttributePoints(),
                inputData.getWeightClass(),
                calculateOverall(
                        inputData.getAttributePoints(),
                        inputData.getWeightClass()
                )
        );

        outputboundary.prepareConfirmSuccessView(outputData);
    }

    /**
     * Creates and saves a gauntlet run from the confirmed fighter data.
     *
     * @param inputData the confirmed fighter data
     * @throws IllegalStateException when the fighter or game settings are missing
     */
    private void createGauntletRun(ConfirmInputData inputData) {
        final CustomFighter fighter = runDataAccess.getCustomFighter();
        final GameSettings settings = runDataAccess.getGameSettings();

        if (fighter == null || settings == null) {
            throw new IllegalStateException(
                    "The configured fighter run is missing."
            );
        }

        final WeightClass weightClass =
                toWeightClass(inputData.getWeightClass());

        fighter.setName(inputData.getFighterName());
        fighter.setWeightClass(weightClass);

        final Division division =
                runDataAccess.getDivision(weightClass);

        final GameRun gameRun = new GameRun(
                fighter,
                division,
                settings.getDifficulty(),
                settings.getRoundsPerFight(),
                settings.isHideOpponentStats()
        );

        runDataAccess.saveGameRun(gameRun);
    }

    /**
     * Converts a weight-class display name to its domain value.
     *
     * @param displayName the weight-class display name
     * @return the matching weight-class value
     * @throws IllegalArgumentException when the display name is not supported
     */
    private WeightClass toWeightClass(String displayName) {
        for (WeightClass weightClass : WeightClass.values()) {
            if (weightClass.getDisplayName().equals(displayName)) {
                return weightClass;
            }
        }

        throw new IllegalArgumentException(
                "Invalid weight class: " + displayName
        );
    }
}
