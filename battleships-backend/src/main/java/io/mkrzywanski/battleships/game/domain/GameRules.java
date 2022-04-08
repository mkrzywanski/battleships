package io.mkrzywanski.battleships.game.domain;

import io.mkrzywanski.battleships.game.domain.snapshot.GameRulesSnapshot;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class GameRules {

    private final AllowedShipDefinitions allowedShipDefinitions;
    private final BoardDimensions boardDimensions;

    public GameRules(final BoardDimensions boardDimensions) {
        this.allowedShipDefinitions = AllowedShipDefinitions.newInstance();
        this.boardDimensions = boardDimensions;
    }

    private GameRules(final AllowedShipDefinitions allowedShipDefinitions, final BoardDimensions boardDimensions) {
        this.allowedShipDefinitions = allowedShipDefinitions;
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
                .shipsAmount(shipAmount)
                .build());
        return this;
    }

    AllowedShipDefinitions getAllowedShipDefinitions() {
        return allowedShipDefinitions;
    }

    BoardDimensions getBoardDimensions() {
        return boardDimensions;
    }

    GameRulesSnapshot toSnapshot() {
        final var allowedShipDefinitions = this.allowedShipDefinitions.asMap();
        return new GameRulesSnapshot(boardDimensions.height(), boardDimensions.width(), allowedShipDefinitions);
    }

    static GameRules fromSnapshot(final GameRulesSnapshot snapshot) {
        final var allowedShipDefinitions = snapshot.getAllowedShipDefinitions();
        final var allowedShipDefinitions1 = AllowedShipDefinitions.newInstance();
        allowedShipDefinitions.forEach((shipLength, shipCount) -> allowedShipDefinitions1.add(AllowedShipDefinition.builder()
                .shipsAmount(shipCount)
                .shipLength(shipLength)
                .build()));
        return new GameRules(allowedShipDefinitions1, BoardDimensions.of(snapshot.getBoardWidth(), snapshot.getBoardHeight()));
    }
}
