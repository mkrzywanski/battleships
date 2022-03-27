package io.mkrzywanski.battleships.domain.exception;

public class ShipBodyOverlappingException extends RuntimeException {
    public ShipBodyOverlappingException(final String message) {
        super(message);
    }
}
