package io.mkrzywanski.battleships.domain.view;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class BoardSnapshot {
    int width;
    int height;
    List<ShipSnapshot> shipSnapshots;
}
