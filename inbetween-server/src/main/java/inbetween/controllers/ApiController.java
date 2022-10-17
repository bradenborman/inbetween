package inbetween.controllers;


import inbetween.models.JoinableGame;
import inbetween.models.Player;
import inbetween.services.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final GameService gameService;

    public ApiController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/joinable-games")
    public ResponseEntity<List<JoinableGame>> joinableGames() {
        List<JoinableGame> games = gameService.findAllJoinableGames();
        return ResponseEntity.ok(games);
    }

    @GetMapping("/player-list-by-uuid")
    public ResponseEntity<List<Player>> playerListByUUID(@RequestParam String uuid) {
        List<Player> playerList = gameService.playerListByUUID(uuid);
        return ResponseEntity.ok(playerList);
    }

}