package io.mkrzywanski.battleships.domain.view;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
public class ShipSnapshot {
    List<ShipCoordinateSnapshot> shipCoordinateSnapshotList;
    UUID playerId;
}
