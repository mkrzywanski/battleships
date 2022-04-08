package io.mkrzywanski.battleships.game.domain.readmodel;

import java.util.Map;
import java.util.UUID;

public record GameView(
        UUID gameId,
        UUID player1Id,
        UUID player2Id,
        UUID currentPlayer,
        Map<UUID, BoardView> boards,
        GameRulesView gameRules) {
}
