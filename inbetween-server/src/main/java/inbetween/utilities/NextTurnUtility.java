package inbetween.utilities;

import inbetween.models.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NextTurnUtility {

    private NextTurnUtility() {
        throw new IllegalStateException("Utility Class Only");
    }

    public static List<Player> sortForNextTurn(List<Player> playerList) {

        Player nextTurnUser = playerList.stream()
                .filter(Player::isPlayersTurn).findAny().orElseThrow(() -> new RuntimeException("No User has a next turn"));

        playerList.remove(nextTurnUser);

        int nextUserId = nextTurnUser.getUserId();

        List<Player> nextUp = playerList.stream().filter(x -> x.getUserId() > nextUserId).collect(Collectors.toList());
        List<Player> wrappedAround = playerList.stream().filter(x -> x.getUserId() < nextUserId).collect(Collectors.toList());

        List<Player> finalList = new ArrayList<>();
        finalList.add(nextTurnUser);
        finalList.addAll(nextUp);
        finalList.addAll(wrappedAround);

        return finalList;
    }


}