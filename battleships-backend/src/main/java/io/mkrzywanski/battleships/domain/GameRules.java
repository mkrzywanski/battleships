package io.mkrzywanski.battleships.domain;

class GameRules {

    private final AllowedShipDefinitions allowedShipDefinitions;
    private final BoardDimensions boardDimensions;

    GameRules(final BoardDimensions boardDimensions) {
        this.allowedShipDefinitions = AllowedShipDefinitions.newInstance();
        this.boardDimensions = boardDimensions;
    }

    public GameRules addShipLengthCount(final int shipLength, final int shipAmount) {
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
