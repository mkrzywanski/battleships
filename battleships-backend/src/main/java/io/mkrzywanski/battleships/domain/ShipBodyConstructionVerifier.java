package io.mkrzywanski.battleships.domain;

import io.mkrzywanski.battleships.domain.exception.IllegalShipPartPositionException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ShipBodyConstructionVerifier {

    static void verifyShipBodyContinuity(final List<Position> shipParts) {
        if (shipParts.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (shipParts.size() > 1) {
            if (isVertical(shipParts)) {
                verifyContinuityInAxis(shipParts, Position::y);
            } else if (isHorizontal(shipParts)) {
                verifyContinuityInAxis(shipParts, Position::x);
            }
        }
    }

    private static void verifyContinuityInAxis(List<Position> positions, Function<Position, Integer> axisValueExtractor) {
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

    private static boolean isVertical(final List<Position> positions) {
        return check(positions, Position::x);
    }

    private static boolean isHorizontal(final List<Position> positions) {
        return check(positions, Position::y);
    }

    private static <T> boolean check(final List<Position> positions, Function<Position, Integer> extractor) {
        return positions.stream()
                .map(extractor)
                .collect(Collectors.toSet())
                .size() == 1;
    }
}
