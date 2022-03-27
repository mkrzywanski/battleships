package io.mkrzywanski.battleships.domain.view;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ShipCoordinateSnapshot {
    PositionSnapshot position;
    boolean isHit;
}
