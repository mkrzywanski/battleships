package io.mkrzywanski.battleships.game.domain;

import java.util.UUID;

public record PlayerId(UUID uuid) {
    public static PlayerId of(final UUID playerId) {
        return new PlayerId(playerId);
    }
}
