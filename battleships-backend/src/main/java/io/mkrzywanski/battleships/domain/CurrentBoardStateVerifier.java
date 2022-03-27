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

    boolean canPlace(Ship ship, PlayerId playerId) {
        int newShipLength = ship.getLength();

        int maxShipAmountForLength = allowedShipDefinitions.getMaxShipContForLength(newShipLength);

        //no rule exists so we cannot place
        if (maxShipAmountForLength == 0) {
            return false;
        }

        var shipsForPlayer = currentShips.stream()
                .collect(Collectors.groupingBy(Ship::getPlayerId))
                .getOrDefault(playerId, List.of());

        if (shipsForPlayer.isEmpty()) {
            return true;
        }

        List<Ship> currentShipsForLength = shipsForPlayer.stream()
                .collect(Collectors.groupingBy(Ship::getLength))
                .getOrDefault(newShipLength, List.of());

        if (currentShipsForLength.isEmpty()) {
            return true;
        }

        int currentShipAmountWithGivenLength = currentShipsForLength.size();
        return currentShipAmountWithGivenLength < maxShipAmountForLength;

    }
}
