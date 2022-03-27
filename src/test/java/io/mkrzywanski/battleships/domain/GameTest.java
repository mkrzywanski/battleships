package io.mkrzywanski.battleships.domain;

import io.mkrzywanski.battleships.domain.data.NewShipBuilder;
import io.mkrzywanski.battleships.domain.exception.GameRulesViolationException;
import io.mkrzywanski.battleships.domain.exception.IllegalShipPartPositionException;
import io.mkrzywanski.battleships.domain.exception.ShipBodyOverlappingException;
import io.mkrzywanski.battleships.domain.view.GameSnapshot;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.params.provider.Arguments.of;

class GameTest {

    Player player1 = new Player(PlayerId.of(UUID.fromString("5a5efc19-2425-4bb4-9de5-b71af27c54f2")));
    Player player2 = new Player(PlayerId.of(UUID.fromString("94f8fe7a-ebfd-4624-9a98-12a4ce6c96cb")));

    @Test
    @DisplayName("Should place ship on board")
    void shouldPlaceShipOnBoard() {
        //given
        GameRules gameRules = new GameRules(BoardDimensions.of(2, 3))
                .addShipLengthCount(3, 1);
        Game game = new Game(player1, player2, gameRules);

        NewShip newShip = NewShipBuilder.newInstance()
                .addShipPart(Position.of(0, 1))
                .addShipPart(Position.of(0, 2))
                .addShipPart(Position.of(0, 3))
                .build();

        //when
        game.placeShip(newShip, player1.playerId());

        //then
        GameSnapshot gameSnapshot = game.toSnapshot();
        assertThat(gameSnapshot.getBoardSnapshot().getShipSnapshots())
                .isNotEmpty()
                .hasSize(1);
    }

    @ParameterizedTest
    @MethodSource("boardAndShips")
    void shouldNotAddShipThatHasNotContinuousPositions(NewShip newShip) {
        //given
        GameRules gameRules = new GameRules(BoardDimensions.of(4, 4));
        Game game = new Game(player1, player2, gameRules);

        //when
        ThrowableAssert.ThrowingCallable code = () -> game.placeShip(newShip, player1.playerId());

        //then
        assertThatCode(code).isInstanceOf(IllegalShipPartPositionException.class);
    }

    static Stream<Arguments> boardAndShips() {
        NewShip yAxisDiscontinuity = NewShipBuilder.newInstance()
                .addShipPart(Position.of(1, 0))
                .addShipPart(Position.of(3, 0))
                .addShipPart(Position.of(4, 0))
                .build();
        NewShip xAxisDiscontinuity = NewShipBuilder.newInstance()
                .addShipPart(Position.of(0, 1))
                .addShipPart(Position.of(0, 3))
                .addShipPart(Position.of(0, 4))
                .build();
        NewShip wrongXAxisOrder = NewShipBuilder.newInstance()
                .addShipPart(Position.of(3, 0))
                .addShipPart(Position.of(2, 0))
                .addShipPart(Position.of(1, 0))
                .build();
        NewShip wrongYAxisOrder = NewShipBuilder.newInstance()
                .addShipPart(Position.of(0, 3))
                .addShipPart(Position.of(0, 2))
                .addShipPart(Position.of(0, 1))
                .build();
        return Stream.of(
                of(yAxisDiscontinuity),
                of(xAxisDiscontinuity),
                of(wrongXAxisOrder),
                of(wrongYAxisOrder)
        );
    }

    @Test
    void shouldRecordTheHitOnShip() {
        //given
        GameRules gameRules = new GameRules(BoardDimensions.of(4, 4))
                .addShipLengthCount(3,1);
        Game game = new Game(player1, player2, gameRules);

        NewShip newShip = NewShipBuilder.newInstance()
                .addShipPart(Position.of(0, 1))
                .addShipPart(Position.of(0, 2))
                .addShipPart(Position.of(0, 3))
                .build();
        game.placeShip(newShip, player1.playerId());

        //when
        boolean hit = game.hit(Position.of(0, 1));

        //then
        assertThat(hit).isTrue();
        GameSnapshot gameSnapshot = game.toSnapshot();
        assertThat(gameSnapshot.getBoardSnapshot().getShipSnapshots())
                .isNotEmpty()
                .hasSize(1)
                .first()
                .matches(shipSnapshot -> shipSnapshot.getShipCoordinateSnapshotList().get(0).isHit());
    }

    @Test
    void shouldNotHitWhenHittingFreePosition() {
        //given
        GameRules gameRules = new GameRules(BoardDimensions.of(4, 4));
        Game game = new Game(player1, player2, gameRules);
        //when
        boolean hit = game.hit(Position.of(0, 1));

        //then
        assertThat(hit).isFalse();
    }

    @ParameterizedTest
    @MethodSource("overlappingShips")
    void shouldFailWhenTryingToPutNewShipButPositionOverlapsWithExistingShip(NewShip newShip) {
        //given
        GameRules gameRules = new GameRules(BoardDimensions.of(4, 4))
                .addShipLengthCount(3, 1);
        Game game = new Game(player1, player2, gameRules);

        NewShip existingShip = NewShipBuilder.newInstance()
                .addShipPart(Position.of(0, 1))
                .addShipPart(Position.of(0, 2))
                .addShipPart(Position.of(0, 3))
                .build();

        game.placeShip(existingShip, player1.playerId());

        //when
        ThrowableAssert.ThrowingCallable code = () -> game.placeShip(newShip, player1.playerId());

        //then
        assertThatCode(code).isExactlyInstanceOf(ShipBodyOverlappingException.class);
    }

    private static Stream<Arguments> overlappingShips() {
        return Stream.of(
                of(
                        NewShipBuilder.newInstance()
                                .addShipPart(Position.of(0, 1))
                                .addShipPart(Position.of(1, 1))
                                .addShipPart(Position.of(2, 1))
                                .build()
                ),
                of(
                        NewShipBuilder.newInstance()
                                .addShipPart(Position.of(0, 2))
                                .addShipPart(Position.of(1, 2))
                                .addShipPart(Position.of(2, 2))
                                .build()
                ),
                of(
                        NewShipBuilder.newInstance()
                                .addShipPart(Position.of(0, 3))
                                .addShipPart(Position.of(1, 3))
                                .addShipPart(Position.of(2, 3))
                                .build()
                )
        );
    }

    @Test
    void player1ShouldWinGameWhenOtherPlayerHasNoShipsAlive() {
        //given
        GameRules gameRules = new GameRules(BoardDimensions.of(4, 4))
                .addShipLengthCount(3, 1);
        Game game = new Game(player1, player2, gameRules);

        NewShip existingShip = NewShipBuilder.newInstance()
                .addShipPart(Position.of(0, 1))
                .addShipPart(Position.of(0, 2))
                .addShipPart(Position.of(0, 3))
                .build();

        NewShip player2Ship = NewShipBuilder.newInstance()
                .addShipPart(Position.of(1, 1))
                .addShipPart(Position.of(1, 2))
                .addShipPart(Position.of(1, 3))
                .build();

        game.placeShip(existingShip, player1.playerId());
        game.placeShip(player2Ship, player2.playerId());

        for (Position position : player2Ship.shipParts()) {
            game.hit(position);
        }

        //when
        Optional<Player> winner = game.getWinner();


        //then
        assertThat(winner).isPresent()
                .contains(player1);
    }

    @Test
    void gameShouldBeFinishedWhenOneOfThePlayersHasNoAliveShips() {
        //given
        GameRules gameRules = new GameRules(BoardDimensions.of(4, 4))
                .addShipLengthCount(3, 1);
        Game game = new Game(player1, player2, gameRules);

        NewShip player1Ship = NewShipBuilder.newInstance()
                .addShipPart(Position.of(0, 1))
                .addShipPart(Position.of(0, 2))
                .addShipPart(Position.of(0, 3))
                .build();

        NewShip player2Ship = NewShipBuilder.newInstance()
                .addShipPart(Position.of(1, 1))
                .addShipPart(Position.of(1, 2))
                .addShipPart(Position.of(1, 3))
                .build();

        game.placeShip(player1Ship, player1.playerId());
        game.placeShip(player2Ship, player2.playerId());

        for (Position position : player2Ship.shipParts()) {
            game.hit(position);
        }

        //when
        boolean gameFinished = game.isFinished();

        //then
        assertThat(gameFinished).isTrue();
    }


    @Test
    void shouldNotBeAbleToPlaceMorShipsThanGameRulesAllowForGivenPlayer() {
        //given
        GameRules gameRules = new GameRules(BoardDimensions.of(4, 4))
                .addShipLengthCount(1, 1);

        Game game = new Game(player1, player2, gameRules);

        NewShip ship = NewShipBuilder.newInstance()
                .addShipPart(Position.of(1, 1))
                .build();

        NewShip anotherShip = NewShipBuilder.newInstance()
                .addShipPart(Position.of(1, 2))
                .build();

        game.placeShip(ship, player1.playerId());

        //when
        ThrowableAssert.ThrowingCallable code = () -> game.placeShip(anotherShip, player1.playerId());

        //then
        assertThatCode(code).isExactlyInstanceOf(GameRulesViolationException.class);
    }
}