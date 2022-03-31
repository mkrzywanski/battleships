package io.mkrzywanski.battleships.domain;

import java.util.Optional;

public interface GameRepository {
    void save(Game game);
    Optional<Game> findByGameId(GameId gameId);
}
