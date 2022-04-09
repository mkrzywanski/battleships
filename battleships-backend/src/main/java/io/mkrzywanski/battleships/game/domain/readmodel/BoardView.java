package io.mkrzywanski.battleships.game.domain.readmodel;

import java.util.List;
import java.util.Set;

public record BoardView(List<ShipView> ships,
                        Set<PositionView> additionalHits) {
}
