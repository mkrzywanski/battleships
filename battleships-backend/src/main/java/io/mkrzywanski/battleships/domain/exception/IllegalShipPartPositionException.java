package io.mkrzywanski.battleships.domain.exception;

public class IllegalShipPartPositionException extends RuntimeException {
    public IllegalShipPartPositionException(final String message) {
        super(message);
    }
}
