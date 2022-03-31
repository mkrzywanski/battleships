package io.mkrzywanski.battleships.domain.view;

import io.mkrzywanski.battleships.domain.GameId;
import io.mkrzywanski.battleships.domain.PlayerId;
import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class GameSnapshot {
    GameId gameId;
    PlayerId player1Id;
    PlayerId player2Id;
    Map<PlayerId, BoardSnapshot> boardSnapshots;
}
