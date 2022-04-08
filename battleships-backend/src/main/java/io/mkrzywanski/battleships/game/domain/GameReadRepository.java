package io.mkrzywanski.battleships.game.domain;

import io.mkrzywanski.battleships.game.domain.readmodel.GameView;

import java.util.Optional;

public interface GameReadRepository {
    Optional<GameView> findByGameId(GameId gameId);
}
