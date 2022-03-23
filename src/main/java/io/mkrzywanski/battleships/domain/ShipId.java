package io.mkrzywanski.battleships.domain;

import java.util.UUID;

record ShipId(UUID shipId) {
    static ShipId of(UUID shipId) {
        return new ShipId(shipId);
    }
}
