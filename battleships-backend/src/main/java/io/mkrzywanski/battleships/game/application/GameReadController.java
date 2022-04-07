package io.mkrzywanski.battleships.game.application;

import io.mkrzywanski.battleships.game.domain.GameId;
import io.mkrzywanski.battleships.game.domain.GameReadRepository;
import io.mkrzywanski.battleships.game.domain.readmodel.GameView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/games")
class GameReadController {

    private final GameReadRepository gameReadRepository;

    GameReadController(final GameReadRepository gameReadRepository) {
        this.gameReadRepository = gameReadRepository;
    }

    @GetMapping("/{gameId}")
    ResponseEntity<GameView> getGameStateById(@PathVariable("gameId") final UUID gameId) {
        final GameId gId = GameId.of(gameId);
        return gameReadRepository.findByGameId(gId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> GameNotFoundException.forGameId(gId));
    }
}
