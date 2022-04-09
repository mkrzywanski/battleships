package io.mkrzywanski.battleships.game.domain.exception;

public class GameException extends RuntimeException {
    GameException(final String message) {
        super(message);
    }
}
