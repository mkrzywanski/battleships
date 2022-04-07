package io.mkrzywanski.battleships.game.domain.exception;

public class IllegalShipPartPositionException extends RuntimeException {
    public IllegalShipPartPositionException(final String message) {
        super(message);
    }
}
