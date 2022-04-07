package io.mkrzywanski.battleships.game.domain.readmodel;

public record ShipCoordinateView(
        PositionView position,
        boolean isHit) {
}
