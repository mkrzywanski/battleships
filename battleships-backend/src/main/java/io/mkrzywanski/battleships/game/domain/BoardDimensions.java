package io.mkrzywanski.battleships.game.domain;

public record BoardDimensions(int width, int height) {
    public static BoardDimensions of(final int width, final int height) {
        return new BoardDimensions(width, height);
    }

    boolean canAccept(final int shipLength) {
        return shipLength > 0 && (shipLength <= width || shipLength <= height);
    }

    boolean contains(final Position position) {
        return position.x() >= 0 && position.x() < width && position.y() >= 0 && position.y() < height;
    }
}
