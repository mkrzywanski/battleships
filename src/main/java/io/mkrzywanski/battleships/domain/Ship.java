package io.mkrzywanski.battleships.domain;

import io.mkrzywanski.battleships.domain.view.ShipSnapshot;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
class Ship {

    private final List<ShipPartCoordinate> coordinates;
    private final PlayerId playerId;

    public boolean isAlive() {
        return !coordinates.stream()
                .allMatch(ShipPartCoordinate::isHit);
    }

    public boolean hasOwnerWithId(PlayerId playerId) {
        return this.playerId.equals(playerId);
    }

    boolean hit(final Position position) {
        return coordinates.stream()
                .filter(shipPartCoordinate -> shipPartCoordinate.equalsByPosition(position))
                .findFirst()
                .orElseThrow()
                .hit();
    }

    public ShipSnapshot toSnapshot() {
        return ShipSnapshot.builder()
                .playerId(playerId.uuid())
                .shipCoordinateSnapshotList(coordinates.stream().map(ShipPartCoordinate::toSnapshot).toList())
                .build();
    }
}
