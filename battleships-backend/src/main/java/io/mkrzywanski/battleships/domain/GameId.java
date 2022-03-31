package io.mkrzywanski.battleships.domain;

import java.util.UUID;

public record GameId(UUID uuid) {
    public static GameId random() {
        return new GameId(UUID.randomUUID());
    }
}
