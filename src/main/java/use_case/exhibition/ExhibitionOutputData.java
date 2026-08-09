package use_case.exhibition;

/**
 * Output data for the Exhibition Match use case: the plain facts of the
 * result, ready for the presenter to format.
 */
public class ExhibitionOutputData {
    private final String winnerName;
    private final String loserName;
    private final String method;
    private final int round;
    private final int secondsInRound;
    private final boolean useCaseFailed;

    public ExhibitionOutputData(String winnerName, String loserName, String method,
                                int round, int secondsInRound, boolean useCaseFailed) {
        this.winnerName = winnerName;
        this.loserName = loserName;
        this.method = method;
        this.round = round;
        this.secondsInRound = secondsInRound;
        this.useCaseFailed = useCaseFailed;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public String getLoserName() {
        return loserName;
    }

    public String getMethod() {
        return method;
    }

    public int getRound() {
        return round;
    }

    public int getSecondsInRound() {
        return secondsInRound;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}
