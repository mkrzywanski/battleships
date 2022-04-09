package io.mkrzywanski.battleships.game.domain.exception;

public class IllegalShipPartPositionException extends GameException {
    public IllegalShipPartPositionException(final String message) {
        super(message);
    }
}
