package io.mkrzywanski.battleships.game.domain.snapshot;

import io.mkrzywanski.battleships.game.domain.Position;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Set;

@Value
@Builder
public class BoardSnapshot {
    List<ShipSnapshot> shipSnapshots;
    GameRulesSnapshot gameRules;
    Set<Position> additionalHits;
}
