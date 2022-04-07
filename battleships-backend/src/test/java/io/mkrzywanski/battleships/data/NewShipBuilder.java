package io.mkrzywanski.battleships.data;

import io.mkrzywanski.battleships.game.domain.NewShip;
import io.mkrzywanski.battleships.game.domain.Position;

import java.util.ArrayList;
import java.util.List;

public class NewShipBuilder {
    
    private final List<Position> positions = new ArrayList<>();

    private NewShipBuilder() {
    }

    public static NewShipBuilder newInstance() {
        return new NewShipBuilder();
    }

    public NewShipBuilder addShipPart(final Position position) {
        positions.add(position);
        return this;
    }

    public NewShip build() {
        return new NewShip(positions);
    }
}
