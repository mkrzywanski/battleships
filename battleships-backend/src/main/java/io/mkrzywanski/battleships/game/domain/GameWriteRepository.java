package io.mkrzywanski.battleships.game.domain;

public interface GameWriteRepository {
    void save(Game game);
}
