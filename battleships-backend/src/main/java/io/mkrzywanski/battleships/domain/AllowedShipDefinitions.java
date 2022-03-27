package io.mkrzywanski.battleships.domain;

import java.util.HashMap;
import java.util.Map;

class AllowedShipDefinitions {

    private final Map<Integer, Integer> allowedShipDefinitions;

    static AllowedShipDefinitions newInstance() {
        return new AllowedShipDefinitions();
    }

    private AllowedShipDefinitions() {
        allowedShipDefinitions = new HashMap<>();
    }

    AllowedShipDefinitions add(AllowedShipDefinition definition) {
        allowedShipDefinitions.put(definition.getShipLength(), definition.getMaxCount());
        return this;
    }

    int getMaxShipContForLength(int shipLength) {
        return allowedShipDefinitions.getOrDefault(shipLength, 0);
    }

}
