package io.mkrzywanski.battleships.domain.game.domain;

import io.mkrzywanski.battleships.data.NewShipBuilder;
import io.mkrzywanski.battleships.game.domain.BoardDimensions;
import io.mkrzywanski.battleships.game.domain.Game;
import io.mkrzywanski.battleships.game.domain.GameRules;
import io.mkrzywanski.battleships.game.domain.NewShip;
import io.mkrzywanski.battleships.game.domain.PlayerId;
import io.mkrzywanski.battleships.game.domain.Position;
import io.mkrzywanski.battleships.game.domain.exception.GameRulesViolationException;
import io.mkrzywanski.battleships.game.domain.exception.IllegalShipPartPositionException;
import io.mkrzywanski.battleships.game.domain.exception.ShipBodyOverlappingException;
import io.mkrzywanski.battleships.game.domain.snapshot.GameSnapshot;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.params.provider.Arguments.of;

class GameTest {

    private final PlayerId player1id = PlayerId.of(UUID.fromString("5a5efc19-2425-4bb4-9de5-b71af27c54f2"));
    private final PlayerId player2id = PlayerId.of(UUID.fromString("94f8fe7a-ebfd-4624-9a98-12a4ce6c96cb"));

    @Test
    @DisplayName("Should place ship on board")
    void shouldPlaceShipOnBoard() {
        //given
        final GameRules gameRules = new GameRules(BoardDimensions.of(2, 3))
                .addShipLengthCount(3, 1);
        final Game game = new Game(player1id, player2id, gameRules);

        final NewShip newShip = NewShipBuilder.newInstance()
                .addShipPart(Position.of(0, 1))
                .addShipPart(Position.of(0, 2))
                .addShipPart(Position.of(0, 3))
                .build();

        //when
        game.placeShip(newShip, player1id);

        //then
        final GameSnapshot gameSnapshot = game.toSnapshot();
        assertThat(gameSnapshot.getBoardSnapshots().get(player1id).getShipSnapshots())
                .isNotEmpty()
                .hasSize(1);
    }

    @ParameterizedTest
    @MethodSource("boardAndShips")
    void shouldNotAddShipThatHasNotContinuousPositions(final NewShip newShip) {
        //given
        final GameRules gameRules = new GameRules(BoardDimensions.of(4, 4));
        final Game game = new Game(player1id, player2id, gameRules);

        //when
        final ThrowableAssert.ThrowingCallable code = () -> game.placeShip(newShip, player1id);

        //then
        assertThatCode(code).isInstanceOf(IllegalShipPartPositionException.class);
    }

    static Stream<Arguments> boardAndShips() {
        final NewShip yAxisDiscontinuity = NewShipBuilder.newInstance()
                .addShipPart(Position.of(1, 0))
                .addShipPart(Position.of(3, 0))
                .addShipPart(Position.of(4, 0))
                .build();
        final NewShip xAxisDiscontinuity = NewShipBuilder.newInstance()
                .addShipPart(Position.of(0, 1))
                .addShipPart(Position.of(0, 3))
                .addShipPart(Position.of(0, 4))
                .build();
        final NewShip wrongXAxisOrder = NewShipBuilder.newInstance()
                .addShipPart(Position.of(3, 0))
                .addShipPart(Position.of(2, 0))
                .addShipPart(Position.of(1, 0))
                .build();
        final NewShip wrongYAxisOrder = NewShipBuilder.newInstance()
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
        final GameRules gameRules = new GameRules(BoardDimensions.of(4, 4))
                .addShipLengthCount(3, 1);
        final Game game = new Game(player1id, player2id, gameRules);

        final NewShip newShip = NewShipBuilder.newInstance()
                .addShipPart(Position.of(0, 1))
                .addShipPart(Position.of(0, 2))
                .addShipPart(Position.of(0, 3))
                .build();

        game.placeShip(newShip, player2id);

        //when
        final boolean hit = game.hit(Position.of(0, 1), player2id);

        //then
        assertThat(hit).isTrue();
        final GameSnapshot gameSnapshot = game.toSnapshot();
        assertThat(gameSnapshot.getBoardSnapshots().get(player2id).getShipSnapshots())
                .isNotEmpty()
                .hasSize(1)
                .first()
                .matches(shipSnapshot -> shipSnapshot.getShipCoordinateSnapshotList().get(0).isHit());
        assertThat(gameSnapshot.getCurrentPlayer()).isEqualTo(player1id);
    }

    @Test
    void shouldNotHitWhenHittingFreePosition() {
        //given
        final GameRules gameRules = new GameRules(BoardDimensions.of(4, 4));
        final Game game = new Game(player1id, player2id, gameRules);

        //when
        final boolean hit = game.hit(Position.of(0, 1), player2id);

        //then
        assertThat(hit).isFalse();
    }

    @ParameterizedTest
    @MethodSource("overlappingShips")
    void shouldFailWhenTryingToPutNewShipButPositionOverlapsWithExistingShip(final NewShip newShip) {
        //given
        final GameRules gameRules = new GameRules(BoardDimensions.of(4, 4))
                .addShipLengthCount(3, 1);
        final Game game = new Game(player1id, player2id, gameRules);

        final NewShip existingShip = NewShipBuilder.newInstance()
                .addShipPart(Position.of(0, 1))
                .addShipPart(Position.of(0, 2))
                .addShipPart(Position.of(0, 3))
                .build();

        game.placeShip(existingShip, player1id);

        //when
        final ThrowableAssert.ThrowingCallable code = () -> game.placeShip(newShip, player1id);

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
        final GameRules gameRules = new GameRules(BoardDimensions.of(4, 4))
                .addShipLengthCount(3, 1);
        final Game game = new Game(player1id, player2id, gameRules);

        final NewShip existingShip = NewShipBuilder.newInstance()
                .addShipPart(Position.of(0, 1))
                .addShipPart(Position.of(0, 2))
                .addShipPart(Position.of(0, 3))
                .build();

        final NewShip player2Ship = NewShipBuilder.newInstance()
                .addShipPart(Position.of(1, 1))
                .addShipPart(Position.of(1, 2))
                .addShipPart(Position.of(1, 3))
                .build();

        game.placeShip(existingShip, player1id);
        game.placeShip(player2Ship, player2id);

        for (Position position : player2Ship.shipParts()) {
            game.hit(position, player2id);
        }

        //when
        final var winner = game.getWinner();

        //then
        assertThat(winner)
                .isPresent()
                .contains(player1id);
    }

    @Test
    void gameShouldBeFinishedWhenOneOfThePlayersHasNoAliveShips() {
        //given
        final GameRules gameRules = new GameRules(BoardDimensions.of(4, 4))
                .addShipLengthCount(3, 1);
        final Game game = new Game(player1id, player2id, gameRules);

        final NewShip player1Ship = NewShipBuilder.newInstance()
                .addShipPart(Position.of(0, 1))
                .addShipPart(Position.of(0, 2))
                .addShipPart(Position.of(0, 3))
                .build();

        final NewShip player2Ship = NewShipBuilder.newInstance()
                .addShipPart(Position.of(1, 1))
                .addShipPart(Position.of(1, 2))
                .addShipPart(Position.of(1, 3))
                .build();

        game.placeShip(player1Ship, player1id);
        game.placeShip(player2Ship, player2id);

        for (Position position : player2Ship.shipParts()) {
            game.hit(position, player2id);
        }

        //when
        final boolean gameFinished = game.isFinished();

        //then
        assertThat(gameFinished).isTrue();
    }


    @Test
    void shouldNotBeAbleToPlaceMorShipsThanGameRulesAllowForGivenPlayer() {
        //given
        final GameRules gameRules = new GameRules(BoardDimensions.of(4, 4))
                .addShipLengthCount(1, 1);

        final Game game = new Game(player1id, player2id, gameRules);

        final NewShip ship = NewShipBuilder.newInstance()
                .addShipPart(Position.of(1, 1))
                .build();

        final NewShip anotherShip = NewShipBuilder.newInstance()
                .addShipPart(Position.of(1, 2))
                .build();

        game.placeShip(ship, player1id);

        //when
        final ThrowableAssert.ThrowingCallable code = () -> game.placeShip(anotherShip, player1id);

        //then
        assertThatCode(code).isExactlyInstanceOf(GameRulesViolationException.class);
    }

    @Test
    void shouldBeAbleToStartGameWhenAllPlayersHaveEnoughShips() {
        final GameRules gameRules = new GameRules(BoardDimensions.of(4, 4))
                .addShipLengthCount(3, 1);

        final Game game = new Game(player1id, player2id, gameRules);

        final NewShip player1Ship = NewShipBuilder.newInstance()
                .addShipPart(Position.of(0, 1))
                .addShipPart(Position.of(0, 2))
                .addShipPart(Position.of(0, 3))
                .build();

        final NewShip player2Ship = NewShipBuilder.newInstance()
                .addShipPart(Position.of(1, 1))
                .addShipPart(Position.of(1, 2))
                .addShipPart(Position.of(1, 3))
                .build();

        game.placeShip(player1Ship, player1id);
        game.placeShip(player2Ship, player2id);

        //when
        final boolean gameFinished = game.canBeStarted();

        //then
        assertThat(gameFinished).isTrue();
    }

    @Test
    void shouldNotBeAbleToStartGameWhenOneOfPlayersDoesNotHaveEnoughShips() {
        final GameRules gameRules = new GameRules(BoardDimensions.of(4, 4))
                .addShipLengthCount(3, 1);

        final Game game = new Game(player1id, player2id, gameRules);

        final NewShip player1Ship = NewShipBuilder.newInstance()
                .addShipPart(Position.of(0, 1))
                .addShipPart(Position.of(0, 2))
                .addShipPart(Position.of(0, 3))
                .build();

        game.placeShip(player1Ship, player1id);

        //when
        final boolean gameFinished = game.canBeStarted();

        //then
        assertThat(gameFinished).isFalse();
    }

    @Test
    void shouldNotBeAbleToStartGameWhenThereAreNoShips() {
        final GameRules gameRules = new GameRules(BoardDimensions.of(4, 4))
                .addShipLengthCount(3, 1);

        final Game game = new Game(player1id, player2id, gameRules);

        //when
        final boolean gameFinished = game.canBeStarted();

        //then
        assertThat(gameFinished).isFalse();
    }

    @Test
    void shouldDumpGameToSnapshot() {
        //given
        final GameRules gameRules = new GameRules(BoardDimensions.of(4, 4))
                .addShipLengthCount(3, 1);

        final Game game = new Game(player1id, player2id, gameRules);
        final GameSnapshot gameSnapshot = game.toSnapshot();

        //when
        final Game rehydratedGame = Game.fromSnapshot(gameSnapshot);

        //then
        assertThat(game).isEqualTo(rehydratedGame);

    }

    @Test
    void whenPlayerHitsFreePositionTheHitShouldBeRecorded() {
        //given
        final GameRules gameRules = new GameRules(BoardDimensions.of(4, 4))
                .addShipLengthCount(3, 1);

        final Game game = new Game(player1id, player2id, gameRules);

        final Position target = Position.of(1, 1);

        //when
        final boolean hit = game.hit(target, player2id);

        assertThat(hit).isFalse();

        final GameSnapshot gameSnapshot = game.toSnapshot();
        assertThat(gameSnapshot.getBoardSnapshots().get(player2id).getAdditionalHits())
                .hasSize(1)
                .first()
                .isEqualTo(target);
    }
}
