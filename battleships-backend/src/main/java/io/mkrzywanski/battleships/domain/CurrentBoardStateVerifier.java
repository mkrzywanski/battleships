package io.mkrzywanski.battleships.domain;

import java.util.List;
import java.util.stream.Collectors;

class CurrentBoardStateVerifier {

    private final List<Ship> currentShips;
    private final AllowedShipDefinitions allowedShipDefinitions;

    CurrentBoardStateVerifier(final List<Ship> currentShips, final AllowedShipDefinitions allowedShipDefinitions) {
        this.currentShips = currentShips;
        this.allowedShipDefinitions = allowedShipDefinitions;
    }

    boolean canPlace(final Ship ship) {
        final int newShipLength = ship.getLength();
        final int maxShipAmountForLength = allowedShipDefinitions.getMaxShipAmountForLength(newShipLength);

        //no rule exists so we cannot place
        if (maxShipAmountForLength == 0) {
            return false;
        }

        if (currentShips.isEmpty()) {
            return true;
        }

        final var currentShipsForLength = currentShips.stream()
                .collect(Collectors.groupingBy(Ship::getLength))
                .getOrDefault(newShipLength, List.of());

        if (currentShipsForLength.isEmpty()) {
            return true;
        }

        final int currentShipAmountWithGivenLength = currentShipsForLength.size();
        return currentShipAmountWithGivenLength < maxShipAmountForLength;

    }
}
