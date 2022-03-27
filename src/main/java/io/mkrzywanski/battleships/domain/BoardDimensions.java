package io.mkrzywanski.battleships.domain;

public record BoardDimensions(int width, int height) {
    public static BoardDimensions of(int width, int height) {
        return new BoardDimensions(width, height);
    }

    boolean canAccept(int shipLength) {
        return shipLength > 0 && (shipLength <= width || shipLength <= height);
    }
}
