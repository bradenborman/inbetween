package inbetween.controllers;


import inbetween.models.JoinableGame;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/joinable-games")
    public ResponseEntity<List<JoinableGame>> joinableGames() {
        JoinableGame jg = new JoinableGame();
        jg.setGameId("1");
        jg.setLobbyName("Nerds");

        JoinableGame jg1 = new JoinableGame();
        jg1.setGameId("2");
        jg1.setLobbyName("Commanders");

        List<JoinableGame> games = Arrays.asList(jg, jg1);

        return ResponseEntity.ok(games);
    }

}