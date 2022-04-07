package io.mkrzywanski.battleships.game.domain;

import java.util.UUID;

public record GameId(UUID uuid) {
    public static GameId random() {
        return new GameId(UUID.randomUUID());
    }

    public static GameId of(final UUID uuid) {
        return new GameId(uuid);
    }
}
