package io.mkrzywanski.battleships.game.domain.exception;

public class PositionOutOfBoardException extends RuntimeException {
    public PositionOutOfBoardException(final String message) {
        super(message);
    }
}
