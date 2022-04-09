package io.mkrzywanski.battleships.game.domain;

import io.mkrzywanski.battleships.game.domain.snapshot.ShipSnapshot;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode
class Ship {

    private final List<ShipPartCoordinate> coordinates;

    Ship(final List<Position> shipParts) {
        ShipBodyConstructionVerifier.verifyShipBodyContinuity(shipParts);
        this.coordinates = shipParts.stream()
                .map(ShipPartCoordinate::new)
                .toList();
    }

    boolean isAlive() {
        return !coordinates.stream()
                .allMatch(ShipPartCoordinate::isHit);
    }

    boolean hit(final Position position) {
        return coordinates.stream()
                .filter(shipPartCoordinate -> shipPartCoordinate.equalsByPosition(position))
                .findFirst()
                .orElseThrow()
                .hit();
    }

    ShipSnapshot toSnapshot() {
        final var coordinateSnapshots = coordinates.stream().map(ShipPartCoordinate::toSnapshot).toList();
        return ShipSnapshot.builder()
                .shipCoordinateSnapshotList(coordinateSnapshots)
                .build();
    }

    List<Position> getPositions() {
        return coordinates.stream()
                .map(ShipPartCoordinate::position)
                .toList();
    }

    int getLength() {
        return coordinates.size();
    }

    static Ship fromSnapshot(final ShipSnapshot shipSnapshot) {
        final var coordinateSnapshots = shipSnapshot.getShipCoordinateSnapshotList()
                .stream()
                .map(ShipPartCoordinate::fromSnapshot).toList();
        return new Ship(coordinateSnapshots.stream().map(ShipPartCoordinate::position).toList());
    }
}
