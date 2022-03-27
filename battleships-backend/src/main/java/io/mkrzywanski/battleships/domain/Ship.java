package io.mkrzywanski.battleships.domain;

import io.mkrzywanski.battleships.domain.view.ShipSnapshot;

import java.util.List;

class Ship {

    private final List<ShipPartCoordinate> coordinates;
    private final PlayerId playerId;

    Ship(final List<Position> shipParts, final PlayerId playerId) {
        ShipBodyConstructionVerifier.verifyShipBodyContinuity(shipParts);
        this.coordinates = shipParts.stream()
                .map(ShipPartCoordinate::new)
                .toList();
        this.playerId = playerId;
    }

    boolean isAlive() {
        return !coordinates.stream()
                .allMatch(ShipPartCoordinate::isHit);
    }

    boolean hasOwnerWithId(PlayerId playerId) {
        return this.playerId.equals(playerId);
    }

    boolean hit(final Position position) {
        return coordinates.stream()
                .filter(shipPartCoordinate -> shipPartCoordinate.equalsByPosition(position))
                .findFirst()
                .orElseThrow()
                .hit();
    }

    ShipSnapshot toSnapshot() {
        return ShipSnapshot.builder()
                .playerId(playerId.uuid())
                .shipCoordinateSnapshotList(coordinates.stream().map(ShipPartCoordinate::toSnapshot).toList())
                .build();
    }

    int getLength() {
        return coordinates.size();
    }

    PlayerId getPlayerId() {
        return playerId;
    }
}
