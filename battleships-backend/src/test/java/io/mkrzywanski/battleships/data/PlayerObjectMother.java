package io.mkrzywanski.battleships.data;

import io.mkrzywanski.battleships.game.domain.PlayerId;

import java.util.UUID;

public final class PlayerObjectMother {
    public static final PlayerId PLAYER_1_ID = new PlayerId(UUID.fromString("1e843d11-a952-4cf1-9046-7960158e74b3"));
    public static final PlayerId PLAYER_2_ID = new PlayerId(UUID.fromString("99e6400a-39fb-489f-9074-c9756eb3f77e"));
}
