package io.mkrzywanski.battleships.game.domain.readmodel;

import java.util.List;

public record GameRulesView(
        int boardHeight,
        int boardWidth,
        List<AllowedShipView> allowedShips) {
}
