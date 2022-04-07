package io.mkrzywanski.battleships.spec;

import io.mkrzywanski.battleships.game.domain.GameId;
import io.mkrzywanski.battleships.game.domain.PlayerId;
import org.springframework.test.web.reactive.server.WebTestClient;

public class GameBodySpecAssertions {

    private final WebTestClient.BodyContentSpec gameView;

    GameBodySpecAssertions(final WebTestClient.BodyContentSpec gameView) {
        this.gameView = gameView;
    }

    public static GameBodySpecAssertions assertThat(final WebTestClient.BodyContentSpec game) {
        return new GameBodySpecAssertions(game);
    }

    public BoardAsserts hasBoardForPlayer(final PlayerId playerId) {
        return new BoardAsserts(gameView, this).forPlayerId(playerId);
    }

    public GameRulesAssert hasGameRules() {
        return new GameRulesAssert(gameView, this);
    }

    public GameBodySpecAssertions hasGameId(final GameId gameId) {
        gameView.jsonPath("$.gameId", gameId.uuid().toString());
        return this;
    }


}
