package io.mkrzywanski.battleships.game.domain;

import io.mkrzywanski.battleships.game.domain.snapshot.PositionSnapshot;

public record Position(int x, int y) {

    public static Position of(final int x, final int y) {
        return new Position(x, y);
    }

    public PositionSnapshot toSnapshot() {
        return PositionSnapshot.builder()
                .x(x)
                .y(y)
                .build();
    }

    public static Position fromSnapshot(final PositionSnapshot positionSnapshot) {
        return new Position(positionSnapshot.getX(), positionSnapshot.getY());
    }
}
