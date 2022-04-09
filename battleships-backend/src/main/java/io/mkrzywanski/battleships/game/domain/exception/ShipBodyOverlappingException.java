package io.mkrzywanski.battleships.game.domain.exception;

public class ShipBodyOverlappingException extends GameException {
    public ShipBodyOverlappingException(final String message) {
        super(message);
    }
}
