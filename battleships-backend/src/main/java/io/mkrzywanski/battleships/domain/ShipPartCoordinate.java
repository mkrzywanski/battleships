package io.mkrzywanski.battleships.domain;

import io.mkrzywanski.battleships.domain.view.ShipCoordinateSnapshot;

class ShipPartCoordinate {

    private final Position position;
    private boolean isHit = false;

    ShipPartCoordinate(final Position position) {
        this.position = position;
    }

    private ShipPartCoordinate(final Position position, final boolean isHit) {
        this.position = position;
        this.isHit = isHit;
    }

    public boolean hit() {
        if (isHit) {
            return false;
        } else {
            isHit = true;
            return true;
        }
    }

    public boolean isHit() {
        return isHit;
    }

    public boolean isNotHit() {
        return !isHit;
    }

    boolean equalsByPosition(final Position position) {
        return this.position.equals(position);
    }

    public ShipCoordinateSnapshot toSnapshot() {
        return ShipCoordinateSnapshot.builder()
                .isHit(isHit)
                .position(position.toSnapshot())
                .build();
    }

    static ShipPartCoordinate fromSnapshot(final ShipCoordinateSnapshot shipCoordinateSnapshot) {
        return new ShipPartCoordinate(Position.fromSnapshot(shipCoordinateSnapshot.getPosition()), shipCoordinateSnapshot.isHit());
    }

    Position position() {
        return position;
    }
}
