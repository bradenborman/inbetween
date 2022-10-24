package inbetween.models.actions;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = BetActionRequest.class),
                @JsonSubTypes.Type(value = CreateLobbyActionRequest.class),
                @JsonSubTypes.Type(value = StartGameActionRequest.class),
                @JsonSubTypes.Type(value = AcknowledgeResultActionRequest.class),
                @JsonSubTypes.Type(value = PassTurnActionRequest.class),
                @JsonSubTypes.Type(value = JoinLobbyActionRequest.class),
                @JsonSubTypes.Type(value = SplitActionRequest.class),
                @JsonSubTypes.Type(value = PassSplitActionRequest.class)
        })
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
public abstract class ActionRequest {

    protected int gameId;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
}
