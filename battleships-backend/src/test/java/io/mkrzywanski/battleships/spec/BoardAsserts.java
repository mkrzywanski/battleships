package io.mkrzywanski.battleships.spec;

import io.mkrzywanski.battleships.game.domain.PlayerId;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.UUID;

public class BoardAsserts {
    private final WebTestClient.BodyContentSpec gameView;
    private final GameBodySpecAssertions parentAssert;
    private UUID playerId;

    BoardAsserts(final WebTestClient.BodyContentSpec gameView, final GameBodySpecAssertions parent) {
        this.gameView = gameView;
        this.parentAssert = parent;
    }

    BoardAsserts forPlayerId(final PlayerId playerId) {
        this.playerId = playerId.uuid();
        return this;
    }

    public BoardAsserts withEmptyShips() {
        gameView.jsonPath("$.boards['" + playerId + "'].ships").isEmpty();
        return this;
    }

    public GameBodySpecAssertions check() {
        return parentAssert;
    }

}
