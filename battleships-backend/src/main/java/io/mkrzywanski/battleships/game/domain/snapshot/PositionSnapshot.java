package io.mkrzywanski.battleships.game.domain.snapshot;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PositionSnapshot {
    int x;
    int y;
}
