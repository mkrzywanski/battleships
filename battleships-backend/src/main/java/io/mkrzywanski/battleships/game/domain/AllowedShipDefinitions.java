package io.mkrzywanski.battleships.game.domain;

import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.counting;

@EqualsAndHashCode
public class AllowedShipDefinitions {

    private final Map<Integer, Integer> allowedShipDefinitions;

    private AllowedShipDefinitions() {
        allowedShipDefinitions = new HashMap<>();
    }

    public static AllowedShipDefinitions newInstance() {
        return new AllowedShipDefinitions();
    }

    public AllowedShipDefinitions add(final AllowedShipDefinition definition) {
        allowedShipDefinitions.put(definition.getShipLength(), definition.getShipsAmount());
        return this;
    }

    int getMaxShipAmountForLength(final int shipLength) {
        return allowedShipDefinitions.getOrDefault(shipLength, 0);
    }

    boolean isSatisfiedBy(final List<Ship> ships) {
        return ships.stream()
                .collect(groupingBy(Ship::getLength, Collectors.collectingAndThen(counting(), Long::intValue)))
                .entrySet().stream()
                .allMatch(entry -> allowedShipDefinitions.getOrDefault(entry.getKey(), 0).equals(entry.getValue()));
    }

    Map<Integer, Integer> asMap() {
        return new HashMap<>(allowedShipDefinitions);
    }
}
