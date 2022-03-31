package io.mkrzywanski.battleships.domain;

import io.mkrzywanski.battleships.domain.view.ShipSnapshot;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@EqualsAndHashCode
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

    private Ship(final PlayerId playerId, final List<ShipPartCoordinate> coordinates) {
        this.coordinates = coordinates;
        this.playerId = playerId;
    }

    boolean isAlive() {
        return !coordinates.stream()
                .allMatch(ShipPartCoordinate::isHit);
    }

    boolean hasOwnerWithId(final PlayerId playerId) {
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

    List<Position> getPositions() {
        return coordinates.stream()
                .map(ShipPartCoordinate::position)
                .toList();
    }

    int getLength() {
        return coordinates.size();
    }

    PlayerId getPlayerId() {
        return playerId;
    }

    static Ship fromSnapshot(final ShipSnapshot shipSnapshot) {
        final UUID playerId = shipSnapshot.getPlayerId();
        final var coordinateSnapshots = shipSnapshot.getShipCoordinateSnapshotList()
                .stream()
                .map(ShipPartCoordinate::fromSnapshot)
                .collect(Collectors.toList());
        return new Ship(new PlayerId(playerId), coordinateSnapshots);
    }
}
