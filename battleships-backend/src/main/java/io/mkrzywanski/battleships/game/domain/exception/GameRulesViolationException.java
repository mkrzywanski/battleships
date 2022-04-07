package io.mkrzywanski.battleships.game.domain.exception;

public class GameRulesViolationException extends RuntimeException {
    public GameRulesViolationException(final String message) {
        super(message);
    }
}
