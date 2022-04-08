package io.mkrzywanski.battleships.game.domain.readmodel;

import java.util.List;

public record ShipView(List<ShipCoordinateView> shipCoordinateSnapshotList) {
}
