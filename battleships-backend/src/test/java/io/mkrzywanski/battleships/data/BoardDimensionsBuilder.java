package io.mkrzywanski.battleships.data;

import io.mkrzywanski.battleships.game.domain.BoardDimensions;

public class BoardDimensionsBuilder {
    public static BoardDimensions fourOnFour() {
        return BoardDimensions.of(4, 4);
    }
}
