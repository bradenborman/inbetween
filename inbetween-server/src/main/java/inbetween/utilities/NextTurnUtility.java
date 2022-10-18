package inbetween.utilities;

import inbetween.models.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NextTurnUtility {

    private NextTurnUtility() {
        throw new IllegalStateException("Utility Class Only");
    }

    public static List<Player> sortForNextTurn(List<Player> playerList, Player currentTurnPlayer) {

        playerList.remove(currentTurnPlayer);

        int nextUserId = currentTurnPlayer.getUserId();

        List<Player> nextUp = playerList.stream().filter(x -> x.getUserId() > nextUserId).collect(Collectors.toList());
        List<Player> wrappedAround = playerList.stream().filter(x -> x.getUserId() < nextUserId).collect(Collectors.toList());

        List<Player> finalList = new ArrayList<>();
        finalList.add(currentTurnPlayer);
        finalList.addAll(nextUp);
        finalList.addAll(wrappedAround);

        return finalList;
    }


}