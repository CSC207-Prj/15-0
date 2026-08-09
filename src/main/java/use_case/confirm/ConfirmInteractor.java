package use_case.confirm;
import java.util.Collections;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
public class ConfirmInteractor implements ConfirmInputBoundary{
    private static final int ATTRIBUTE_COUNT = 6;
    private static final int ATTRIBUTE_MIN = 0;
    private static final int ATTRIBUTE_MAX= 100;
    private static final double TEN_PERCENT = 0.10;
    private static final double FIFTEEN_PERCENT = 0.15;
    private static final double TWENTY_PERCENT = 0.20;
    private static final double TWENTY_FIVE_PERCENT = 0.25;
    private static final double THIRTY_PERCENT = 0.30;


    private static final List<String> weightClasses = new ArrayList<>();
    static {
        Collections.addAll(weightClasses, "Flyweight", "Bantamweight", "Featherweight", "Lightweight", "Welterweight", "Middleweight", "Light Heavyweight", "Heavyweight");
    }


    /**
     * Weight order for attribute: Striking, Defence, Takedowns, Height, Reach, Cardio
     */

    private static final List<Double> FAST_WEIGHTS = new ArrayList<>();
    private static final List<Double> BALANCED_WEIGHTS = new ArrayList<>();
    private static final List<Double> WELTERWEIGHT_WEIGHTS = new ArrayList<>();
    private static final List<Double> MIDDLEWEIGHT_WEIGHTS = new ArrayList<>();
    private static final List<Double> HEAVY_WEIGHTS = new ArrayList<>();


    static {
        //Fly and Bantam
        Collections.addAll(FAST_WEIGHTS, TWENTY_PERCENT, TWENTY_PERCENT, FIFTEEN_PERCENT, TEN_PERCENT, TEN_PERCENT, TWENTY_FIVE_PERCENT);
        //Feather and Light
        Collections.addAll(BALANCED_WEIGHTS, TWENTY_PERCENT, TWENTY_PERCENT, TWENTY_PERCENT, TEN_PERCENT, TEN_PERCENT, TWENTY_PERCENT);
        Collections.addAll(WELTERWEIGHT_WEIGHTS, TWENTY_FIVE_PERCENT, TWENTY_PERCENT, TWENTY_PERCENT, TEN_PERCENT, TEN_PERCENT, FIFTEEN_PERCENT);
        Collections.addAll(MIDDLEWEIGHT_WEIGHTS, TWENTY_FIVE_PERCENT, TWENTY_PERCENT, FIFTEEN_PERCENT, FIFTEEN_PERCENT, FIFTEEN_PERCENT, TEN_PERCENT);
        //Light and Heavy
        Collections.addAll(HEAVY_WEIGHTS, THIRTY_PERCENT, TWENTY_PERCENT, TEN_PERCENT, FIFTEEN_PERCENT, FIFTEEN_PERCENT, TEN_PERCENT);
    }


    private final ConfirmOutputBoundary outputboundary;
    private Random random = new Random();

    public ConfirmInteractor(ConfirmOutputBoundary outputboundary) {
        this.outputboundary = outputboundary;
        this.random = new Random();

    }
    private boolean validAttributes(ConfirmInputData inputData) {
        if (inputData == null ||inputData.getAttributePoints() == null || inputData.getAttributePoints().size() != ATTRIBUTE_COUNT) {
            outputboundary.prepareFailureView("All 6 values must be assigned");
            return false;
        }
        for(String attribute: inputData.getAttributePoints()) {
            final int value = Integer.parseInt(attribute);
            if (attribute.isEmpty() || value < ATTRIBUTE_MIN|| value > ATTRIBUTE_MAX) {
                outputboundary.prepareFailureView("Attribute Invalid");
                return false;
            }
        }
        return true;
    }

    private int calculateOverall(List<String> attributePoints, String weightClass) {
        List<Double> weights = getWeights(weightClass);
        double totalWeight = 0;

        for (int index = 0; index < ATTRIBUTE_COUNT; index++) {
            final double attributeValue = Double.parseDouble(attributePoints.get(index));
            totalWeight += attributeValue * weights.get(index);
        }
        return (int) Math.round(totalWeight);
    }

    private List<Double> getWeights(String weightClasses){
        switch (weightClasses) {
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
            case "light Heavyweight":
                return HEAVY_WEIGHTS;
            default:
                throw new IllegalArgumentException("Invalid weight classes");
        }

    }
    @Override
    public void spin(ConfirmInputData inputData) {
        if(!validAttributes(inputData)) {
            return;
        }
        final String weightClass = weightClasses.get(random.nextInt(weightClasses.size()));
        final int overall = calculateOverall(inputData.getAttributePoints(), weightClass);
        final ConfirmOutputData outputData = new ConfirmOutputData(inputData.getFighterName(),inputData.getAttributePoints(), weightClass, overall);
        outputboundary.prepareSpinSuccessView(outputData);

    }

    @Override
    public void confirm(ConfirmInputData inputData) {
        if (inputData == null || inputData.getFighterName() == null || inputData.getFighterName().isEmpty() || inputData.getAttributePoints().isEmpty()) {
            outputboundary.prepareFailureView("Fighter name cannot be empty");
            return;
        }
        if (!validAttributes(inputData)) {
            return;
        }
        if(inputData.getWeightClass() == null || !weightClasses.contains(inputData.getWeightClass())) {
            outputboundary.prepareFailureView("Spin weight class before confirming");
            return;
        }
        final ConfirmOutputData outputData = new ConfirmOutputData(inputData.getFighterName(),inputData.getAttributePoints(), inputData.getWeightClass(),calculateOverall(inputData.getAttributePoints(), inputData.getWeightClass()));
        outputboundary.prepareConfirmSuccessView(outputData);
    }
}
