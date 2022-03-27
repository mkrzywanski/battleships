package io.mkrzywanski.battleships.domain;

import io.mkrzywanski.battleships.domain.view.PositionSnapshot;

public record Position(int x, int y) {
    static Position of(int x, int y) {
        return new Position(x, y);
    }

    public PositionSnapshot toSnapshot() {
        return PositionSnapshot.builder()
                .x(x)
                .y(y)
                .build();
    }
}