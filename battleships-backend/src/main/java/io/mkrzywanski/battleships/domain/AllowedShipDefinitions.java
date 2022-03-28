package io.mkrzywanski.battleships.domain;

import java.util.HashMap;
import java.util.Map;

class AllowedShipDefinitions {

    private final Map<Integer, Integer> allowedShipDefinitions;

    private AllowedShipDefinitions() {
        allowedShipDefinitions = new HashMap<>();
    }

    static AllowedShipDefinitions newInstance() {
        return new AllowedShipDefinitions();
    }

    AllowedShipDefinitions add(final AllowedShipDefinition definition) {
        allowedShipDefinitions.put(definition.getShipLength(), definition.getMaxCount());
        return this;
    }

    int getMaxShipContForLength(final int shipLength) {
        return allowedShipDefinitions.getOrDefault(shipLength, 0);
    }

}
