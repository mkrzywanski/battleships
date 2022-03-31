package io.mkrzywanski.battleships.domain.view;

import lombok.Value;

import java.util.Map;

@Value
public class GameRulesSnapshot {
    int boardHeight;
    int boardWidth;
    Map<Integer, Integer> allowedShipDefinitions;
}
