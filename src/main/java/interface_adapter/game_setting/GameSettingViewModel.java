package interface_adapter.game_setting;

import interface_adapter.ViewModel;

/**
 * ViewModel for the Game Settings screen.
 */
public class GameSettingViewModel extends ViewModel {
    public static final String VIEW_NAME = "settings";

    private GameSettingState state = new GameSettingState();

    public GameSettingViewModel() {
        super(VIEW_NAME);
    }

    public GameSettingState getState() {
        return state;
    }

    public void setState(GameSettingState state) {
        final GameSettingState oldState = this.state;

        if (state == null) {
            this.state = new GameSettingState();
        }
        else {
            this.state = state;
        }

        firePropertyChanged(oldState, this.state);
    }

    public void firePropertyChanged() {
        firePropertyChanged(null, state);
    }
}
