package io.mkrzywanski.battleships.domain;

import io.mkrzywanski.battleships.domain.view.BoardSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

class Board {

    private final int width;
    private final int height;
    private final List<Ship> shipList;
    private final Map<Position, Ship> shipMap;

    Board(final int width, final int height) {
        this.width = width;
        this.height = height;
        this.shipList = new ArrayList<>();
        this.shipMap = new HashMap<>();
    }

    boolean hit(Position position) {
        return Optional.ofNullable(shipMap.get(position))
                .map(ship -> ship.hit(position))
                .orElse(false);
    }

    List<Ship> aliveShipsFor(PlayerId playerId) {
        return shipList.stream()
                .filter(ship -> ship.hasOwnerWithId(playerId))
                .filter(Ship::isAlive)
                .collect(Collectors.toList());
    }

    boolean hasAliveShipsFor(PlayerId playerId) {
        return !aliveShipsFor(playerId).isEmpty();
    }

    void placeShip(final NewShip newShip, final PlayerId playerId) {
        var shipParts = newShip.shipParts();

        for (Position position : shipParts) {
            verifyWithinBoard(position);
        }
        verifyShipBodyContinuity(shipParts);

        var shipPartCoordinates = shipParts.stream()
                .map(ShipPartCoordinate::new)
                .toList();

        Ship ship = new Ship(shipPartCoordinates, playerId);
        shipList.add(ship);
        shipParts.forEach(position -> shipMap.put(position, ship));
    }

    private void verifyShipBodyContinuity(final List<Position> shipParts) {
        if (shipParts.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (shipParts.size() > 1) {
            if (isVertical(shipParts)) {
               verifyContinuityInAxis(shipParts, Position::y);
            } else if(isHorizontal(shipParts)) {
                verifyContinuityInAxis(shipParts, Position::x);
            }
        }
    }

    private void verifyContinuityInAxis(List<Position> positions, Function<Position, Integer> axisValueExtractor) {
        Iterator<Position> iterator = positions.iterator();
        Position currentPosition = iterator.next();
        while (iterator.hasNext()) {
            Position nextPosition = iterator.next();
            Integer previous = axisValueExtractor.apply(currentPosition);
            Integer next = axisValueExtractor.apply(nextPosition);
            if (previous + 1 != next) {
                throw new IllegalShipPartPositionException("");
            }
            currentPosition = nextPosition;
        }
    }

    private boolean isVertical(final List<Position> positions) {
        return check(positions, Position::x);
    }

    private boolean isHorizontal(final List<Position> positions) {
        return check(positions, Position::y);
    }

    private <T> boolean check(final List<Position> positions, Function<Position, Integer> extractor) {
        return positions.stream().map(extractor).collect(Collectors.toSet()).size() == 1;
    }

    private void verifyWithinBoard(final Position position) {
        verifyXPosition(position);
        verifyYPosition(position);
    }

    private void verifyYPosition(final Position position) {
        int y = position.y();
        if (y < 0 || y > height) {
            throw new PositionOutOfBoardException("Position " + position + " is out of board. Maximum width is " + width);
        }
    }

    private void verifyXPosition(final Position position) {
        int x = position.x();
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
