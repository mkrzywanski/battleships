package io.mkrzywanski.battleships.game.domain.snapshot;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ShipCoordinateSnapshot {
    PositionSnapshot position;
    boolean isHit;
}
