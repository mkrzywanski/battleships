package io.mkrzywanski.battleships.game.domain;

import io.mkrzywanski.battleships.game.domain.exception.GameRulesViolationException;
import io.mkrzywanski.battleships.game.domain.exception.PositionOutOfBoardException;
import io.mkrzywanski.battleships.game.domain.exception.ShipBodyOverlappingException;
import io.mkrzywanski.battleships.game.domain.snapshot.BoardSnapshot;
import lombok.EqualsAndHashCode;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode
class Board {

    private final List<Ship> shipList;
    private final Map<Position, Ship> shipMap;
    private final GameRules gameRules;
    private final Set<Position> additionalHits;

    Board(final GameRules gameRules) {
        this.gameRules = gameRules;
        this.shipList = new ArrayList<>();
        this.shipMap = new HashMap<>();
        this.additionalHits = new HashSet<>();
    }

    Board(final List<Ship> shipList, final GameRules gameRules,
          final Set<Position> additionalPositionHits) {
        this.shipList = shipList;
        this.gameRules = gameRules;
        this.shipMap = shipList.stream()
                .flatMap(ship -> ship.getPositions().stream().map(position -> new SimpleImmutableEntry<>(position, ship)))
                .collect(Collectors.toMap(SimpleImmutableEntry::getKey, SimpleImmutableEntry::getValue));
        this.additionalHits = additionalPositionHits;
    }

    boolean hit(final Position position) {
        if (shipMap.containsKey(position)) {
            return shipMap.get(position).hit(position);
        } else if (gameRules.getBoardDimensions().contains(position)) {
            additionalHits.add(position);
            return false;
        } else {
            return false;
        }

    }

    boolean hasNoAliveShips() {
        return shipList.stream()
                .noneMatch(Ship::isAlive);
    }

    void placeShip(final NewShip newShip) {
        final var shipParts = newShip.shipParts();

        for (Position position : shipParts) {
            verifyWithinBoard(position);
        }

        verifyPositionsDoNotOverlap(shipParts);

        final Ship ship = new Ship(shipParts);

        final boolean cannotPlace = !new CurrentBoardStateVerifier(shipList, gameRules.getAllowedShipDefinitions()).canPlace(ship);
        if (cannotPlace) {
            throw new GameRulesViolationException("");
        }

        shipList.add(ship);
        shipParts.forEach(position -> shipMap.put(position, ship));
    }

    private void verifyPositionsDoNotOverlap(final List<Position> shipParts) {
        final var takenPositions = shipMap.keySet();
        final boolean isOverlapping = shipParts.stream()
                .anyMatch(takenPositions::contains);
        if (isOverlapping) {
            throw new ShipBodyOverlappingException("");
        }
    }

    private void verifyWithinBoard(final Position position) {
        verifyXPosition(position);
        verifyYPosition(position);
    }

    private void verifyYPosition(final Position position) {
        final int y = position.y();
        final int height = gameRules.getBoardDimensions().height();
        if (y < 0 || y > height) {
            throw new PositionOutOfBoardException("Position " + position + " is out of board. Maximum width is " + height);
        }
    }

    private void verifyXPosition(final Position position) {
        final int x = position.x();
        final int width = gameRules.getBoardDimensions().width();
        if (x < 0 || x > width) {
            throw new PositionOutOfBoardException("Position " + position + " is not within bounds. Width - [0," + width + "");
        }
    }

    public BoardSnapshot toSnapshot() {
        return BoardSnapshot.builder()
                .shipSnapshots(shipList.stream().map(Ship::toSnapshot).toList())
                .gameRules(gameRules.toSnapshot())
                .additionalHits(additionalHits)
                .build();
    }

    boolean hasEnoughShips() {
        if (shipList.isEmpty()) {
            return false;
        } else {
            return gameRules.getAllowedShipDefinitions().isSatisfiedBy(shipList);
        }
    }

    static Builder builder() {
        return new Builder();
    }

    private static class Builder {
        private List<Ship> shipList;
        private GameRules gameRules;
        private Set<Position> additionalHitPositions;

        private Builder() {
        }

        private Builder shipList(final List<Ship> shipList) {
            this.shipList = shipList;
            return this;
        }

        private Builder gameRules(final GameRules gameRules) {
            this.gameRules = gameRules;
            return this;
        }

        private Builder additionalHitPositions(final Set<Position> additional) {
            this.additionalHitPositions = additional;
            return this;
        }

        private Board build() {
            return new Board(shipList, gameRules, additionalHitPositions);
        }

    }

    static Board fromSnapshot(final BoardSnapshot boardSnapshot) {
        final var shipSnapshots = boardSnapshot.getShipSnapshots();
        final var ships = shipSnapshots.stream().map(Ship::fromSnapshot).toList();
        final var gameRules = GameRules.fromSnapshot(boardSnapshot.getGameRules());
        return Board.builder()
                .gameRules(gameRules)
                .shipList(ships)
                .additionalHitPositions(boardSnapshot.getAdditionalHits())
                .build();
    }
}
