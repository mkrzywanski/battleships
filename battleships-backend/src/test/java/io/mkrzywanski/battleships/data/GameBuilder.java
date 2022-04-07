package io.mkrzywanski.battleships.data;

import io.mkrzywanski.battleships.game.domain.Game;
import io.mkrzywanski.battleships.game.domain.GameRules;
import io.mkrzywanski.battleships.game.domain.PlayerId;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
@ConditionalOnProperty
public class GameBuilder {

    private PlayerId player1Id = PlayerObjectMother.PLAYER_1_ID;
    private PlayerId player2Id = PlayerObjectMother.PLAYER_2_ID;
    private GameRules gameRules = GameRulesBuilder.simple();
    private BoardBuilder boardBuilder;

    private GameBuilder() {
    }

    public static GameBuilder newGame() {
        return new GameBuilder();
    }

    public static Game withoutShips() {
        final GameRules simple = GameRulesBuilder.simple();
        return new Game(PlayerObjectMother.PLAYER_1_ID, PlayerObjectMother.PLAYER_2_ID, simple);
    }

    public Game build() {
        return new Game(player1Id, player2Id, gameRules);
    }

}
