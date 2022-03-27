package io.mkrzywanski.battleships.domain;

import java.util.HashMap;
import java.util.Map;

class GameRules {

    private final AllowedShipDefinitions allowedShipDefinitions;
    private final BoardDimensions boardDimensions;

    GameRules(final BoardDimensions boardDimensions) {
        this.allowedShipDefinitions = AllowedShipDefinitions.newInstance();
        this.boardDimensions = boardDimensions;
    }

    public GameRules addShipLengthCount(int shipLength, int shipAmount) {
        if (shipAmount <= 0) {
            throw new IllegalArgumentException();
        }
        if (!boardDimensions.canAccept(shipLength)) {
            throw new IllegalArgumentException();
        }

        this.allowedShipDefinitions.add(AllowedShipDefinition.builder()
                .shipLength(shipLength)
                .maxCount(shipAmount)
                .build());
        return this;
    }

    AllowedShipDefinitions getAllowedShipDefinitions() {
        return allowedShipDefinitions;
    }

    BoardDimensions getBoardDimensions() {
        return boardDimensions;
    }
}
