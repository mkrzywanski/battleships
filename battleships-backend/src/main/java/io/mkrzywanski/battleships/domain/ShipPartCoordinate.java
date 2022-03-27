package io.mkrzywanski.battleships.domain;

import io.mkrzywanski.battleships.domain.view.ShipCoordinateSnapshot;

class ShipPartCoordinate {

    private final Position position;
    private boolean isHit = false;

    ShipPartCoordinate(final Position position) {
        this.position = position;
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

    boolean equalsByPosition(Position position) {
        return this.position.equals(position);
    }

    public ShipCoordinateSnapshot toSnapshot() {
        return ShipCoordinateSnapshot.builder()
                .isHit(isHit)
                .position(position.toSnapshot())
                .build();
    }
}
