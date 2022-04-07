package io.mkrzywanski.battleships.data;

import io.mkrzywanski.battleships.game.domain.BoardDimensions;
import io.mkrzywanski.battleships.game.domain.GameRules;

import static io.mkrzywanski.battleships.data.BoardDimensionsBuilder.*;

class GameRulesBuilder {

    private BoardDimensions boardDimensions = fourOnFour();

    public static GameRules simple() {
        final GameRules gameRules = new GameRules(fourOnFour());
        gameRules.addShipLengthCount(3, 1);
        return gameRules;
    }
}
