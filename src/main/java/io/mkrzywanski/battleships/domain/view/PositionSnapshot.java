package io.mkrzywanski.battleships.domain.view;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PositionSnapshot {
    int x;
    int y;
}
