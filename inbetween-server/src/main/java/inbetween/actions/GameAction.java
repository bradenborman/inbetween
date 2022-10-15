package inbetween.actions;

import inbetween.models.actions.ActionRequest;

public interface GameAction {

    public boolean hasWork(String actionString);

    public void perform(ActionRequest actionRequest);

}
