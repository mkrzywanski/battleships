package io.mkrzywanski.battleships.domain;

import java.util.UUID;

public record PlayerId(UUID uuid) {
    static PlayerId of(UUID playerId) {
        return new PlayerId(playerId);
    }
}
