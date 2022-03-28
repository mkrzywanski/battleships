package io.mkrzywanski.battleships.domain;

import io.mkrzywanski.battleships.domain.exception.GameRulesViolationException;
import io.mkrzywanski.battleships.domain.exception.PositionOutOfBoardException;
import io.mkrzywanski.battleships.domain.exception.ShipBodyOverlappingException;
import io.mkrzywanski.battleships.domain.view.BoardSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

class Board {

    private final int width;
    private final int height;
    private final List<Ship> shipList;
    private final Map<Position, Ship> shipMap;
    private final GameRules gameRules;


    Board(final GameRules gameRules) {
        this.width = gameRules.getBoardDimensions().width();
        this.height = gameRules.getBoardDimensions().height();
        this.gameRules = gameRules;
        this.shipList = new ArrayList<>();
        this.shipMap = new HashMap<>();
    }

    boolean hit(final Position position) {
        return Optional.ofNullable(shipMap.get(position))
                .map(ship -> ship.hit(position))
                .orElse(false);
    }

    List<Ship> aliveShipsFor(final PlayerId playerId) {
        return shipList.stream()
                .filter(ship -> ship.hasOwnerWithId(playerId))
                .filter(Ship::isAlive)
                .collect(Collectors.toList());
    }

    boolean hasAliveShipsFor(final PlayerId playerId) {
        return !aliveShipsFor(playerId).isEmpty();
    }

    void placeShip(final NewShip newShip, final PlayerId playerId) {
        final var shipParts = newShip.shipParts();

        for (Position position : shipParts) {
            verifyWithinBoard(position);
        }

        verifyPositionsDoNotOverlap(shipParts);

        final Ship ship = new Ship(shipParts, playerId);

        final boolean cannotPlace = !new CurrentBoardStateVerifier(shipList, gameRules.getAllowedShipDefinitions()).canPlace(ship, playerId);
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
        if (y < 0 || y > height) {
            throw new PositionOutOfBoardException("Position " + position + " is out of board. Maximum width is " + width);
        }
    }

    private void verifyXPosition(final Position position) {
        final int x = position.x();
        if (x < 0 || x > width) {
            throw new PositionOutOfBoardException("Position " + position + " is not within bounds. Width - [0," + width + "");
        }
    }

    public BoardSnapshot toSnapshot() {
        return BoardSnapshot.builder()
                .width(width)
                .height(height)
                .shipSnapshots(shipList.stream().map(Ship::toSnapshot).toList())
                .build();
    }
}
