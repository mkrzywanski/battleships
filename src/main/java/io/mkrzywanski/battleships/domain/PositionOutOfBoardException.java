package io.mkrzywanski.battleships.domain;

public class PositionOutOfBoardException extends RuntimeException {
    PositionOutOfBoardException(final String message) {
        super(message);
    }
}
