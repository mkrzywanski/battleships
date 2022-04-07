package io.mkrzywanski.battleships.game.domain.snapshot;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ShipSnapshot {
    List<ShipCoordinateSnapshot> shipCoordinateSnapshotList;
}
