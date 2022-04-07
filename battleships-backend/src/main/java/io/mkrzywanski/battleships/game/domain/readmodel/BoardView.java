package io.mkrzywanski.battleships.game.domain.readmodel;

import lombok.Value;

import java.util.List;
import java.util.Set;

@Value
public class BoardView {
    List<ShipView> ships;
    Set<PositionView> additionalHits;
}
