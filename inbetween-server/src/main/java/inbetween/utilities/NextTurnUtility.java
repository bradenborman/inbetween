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

        int currentTurnPlayerUserId = currentTurnPlayer.getUserId();

        List<Player> nextUp = playerList.stream().filter(x -> x.getUserId() > currentTurnPlayerUserId).collect(Collectors.toList());
        List<Player> wrappedAround = playerList.stream().filter(x -> x.getUserId() < currentTurnPlayerUserId).collect(Collectors.toList());

        List<Player> finalList = new ArrayList<>();
        finalList.add(currentTurnPlayer);
        finalList.addAll(nextUp);
        finalList.addAll(wrappedAround);

        boolean someoneHasPoints = playerList.stream().anyMatch(x -> x.getScore() > 0);

        //If the next person up doesn't have any 1 point or less to bet, go to the next as long as there is someone else that has points
        if (finalList.size() > 1 && finalList.get(1).getScore() <= 1 && someoneHasPoints) {
            sortForNextTurn(finalList, finalList.get(1));
        }

        return finalList;
    }


}