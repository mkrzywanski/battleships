package io.mkrzywanski.battleships.domain.view;

import io.mkrzywanski.battleships.domain.PlayerId;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GameSnapshot {
    PlayerId player1Id;
    PlayerId player2Id;
    BoardSnapshot boardSnapshot;
}
