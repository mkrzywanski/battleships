package io.mkrzywanski.battleships.game.application;

import io.mkrzywanski.battleships.game.domain.GameId;
import lombok.Getter;

import static java.lang.String.format;

@Getter
class GameNotFoundException extends RuntimeException {

    private final GameId gameId;

    GameNotFoundException(final GameId gameId) {
        super(format("Game with id %s not found", gameId.uuid()));
        this.gameId = gameId;
    }

    static GameNotFoundException forGameId(final GameId gameId) {
        return new GameNotFoundException(gameId);
    }
}
