package io.mkrzywanski.battleships.domain;

import io.mkrzywanski.battleships.domain.data.NewShipBuilder;
import io.mkrzywanski.battleships.domain.view.GameSnapshot;
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

class GameTest {

    Player player1 = new Player(PlayerId.of(UUID.fromString("5a5efc19-2425-4bb4-9de5-b71af27c54f2")));
    Player player2 = new Player(PlayerId.of(UUID.fromString("94f8fe7a-ebfd-4624-9a98-12a4ce6c96cb")));

    @Test
    @DisplayName("Should place ship on board")
    void shouldPlaceShipOnBoard() {
        //given
        Board board = new Board(2, 3);
        Game game = new Game(player1, player2, board);

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
        Board board = new Board(4, 4);
        Game game = new Game(player1, player2, board);

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
                Arguments.of(yAxisDiscontinuity),
                Arguments.of(xAxisDiscontinuity),
                Arguments.of(wrongXAxisOrder),
                Arguments.of(wrongYAxisOrder)
        );
    }

    @Test
    void shouldRecordTheHitOnShip() {
        //given
        Board board = new Board(4, 4);
        Game game = new Game(player1, player2, board);

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
        Board board = new Board(4, 4);
        Game game = new Game(player1, player2, board);

        //when
        boolean hit = game.hit(Position.of(0, 1));

        //then
        assertThat(hit).isFalse();
    }
}