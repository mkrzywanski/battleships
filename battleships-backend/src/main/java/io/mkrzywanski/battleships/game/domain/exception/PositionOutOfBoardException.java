package io.mkrzywanski.battleships.game.domain.exception;

public class PositionOutOfBoardException extends GameException {
    public PositionOutOfBoardException(final String message) {
        super(message);
    }
}
