package io.mkrzywanski.battleships.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
class AllowedShipDefinition {
    private final int shipLength;
    private final int maxCount;
}
