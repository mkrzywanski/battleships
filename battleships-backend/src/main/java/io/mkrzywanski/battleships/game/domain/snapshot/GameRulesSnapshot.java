package io.mkrzywanski.battleships.game.domain.snapshot;

import lombok.Value;

import java.util.Map;

@Value
public class GameRulesSnapshot {
    int boardHeight;
    int boardWidth;
    Map<Integer, Integer> allowedShipDefinitions;
}
