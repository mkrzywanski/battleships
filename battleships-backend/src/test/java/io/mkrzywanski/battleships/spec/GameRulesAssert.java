package io.mkrzywanski.battleships.spec;

import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Map;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;

public class GameRulesAssert {
    private final WebTestClient.BodyContentSpec gameView;
    private final GameBodySpecAssertions parentAssert;

    GameRulesAssert(final WebTestClient.BodyContentSpec gameView, final GameBodySpecAssertions parentAssert) {
        this.gameView = gameView;
        this.parentAssert = parentAssert;
    }

    public GameRulesAssert withWidth(final int width) {
        gameView.jsonPath("$.gameRules.boardWidth").value(equalTo(width));
        return this;
    }

    public GameRulesAssert withHeight(final int height) {
        gameView.jsonPath("$.gameRules.boardHeight").value(equalTo(height));
        return this;
    }

    public GameRulesAssert withAllowedShipCount(final int count, final int shipLength) {
        final var expectedAllowedShipDefinition = Map.of(
                "shipCount", count,
                "shipLength", shipLength
        );
        gameView.jsonPath("$.gameRules.allowedShips").value(contains(expectedAllowedShipDefinition));
        return this;
    }

    public GameBodySpecAssertions check() {
        return parentAssert;
    }
}
