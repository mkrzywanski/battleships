package io.mkrzywanski.battleships.game.domain.exception;

public class GameRulesViolationException extends GameException {
    public GameRulesViolationException(final String message) {
        super(message);
    }
}
