package io.mkrzywanski.battleships.game.domain.snapshot;

import io.mkrzywanski.battleships.game.domain.GameId;
import io.mkrzywanski.battleships.game.domain.PlayerId;
import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class GameSnapshot {
    GameId gameId;
    PlayerId player1Id;
    PlayerId player2Id;
    PlayerId currentPlayer;
    Map<PlayerId, BoardSnapshot> boardSnapshots;
}
