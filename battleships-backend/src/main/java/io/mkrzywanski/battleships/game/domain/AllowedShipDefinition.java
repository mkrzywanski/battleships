package io.mkrzywanski.battleships.game.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Value
class AllowedShipDefinition {
    int shipLength;
    int shipsAmount;
}
