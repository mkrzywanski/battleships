package io.mkrzywanski.battleships.domain;

import io.mkrzywanski.battleships.domain.view.GameSnapshot;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.util.Optional;

@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Game {

    private final Player player1;
    private final Player player2;
    private final Board board;

    public Game(final Player player1, final Player player2, final GameRules gameRules) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = new Board(gameRules);
    }

    public boolean isFinished() {
        return getWinner().isPresent();
    }

    public Optional<Player> getWinner() {
        return board.hasAliveShipsFor(player1.playerId()) ? Optional.of(player1) : Optional.of(player2);
    }

    public boolean hit(final Position position) {
        return board.hit(position);
    }

    public void placeShip(final NewShip newShip, final PlayerId playerId) {
        board.placeShip(newShip, playerId);
    }

    public boolean canBeStarted() {
        return board.hasEnoughShips();
    }

    public GameSnapshot toSnapshot() {
        return GameSnapshot.builder()
                .player1Id(player1.playerId())
                .player2Id(player2.playerId())
                .boardSnapshot(board.toSnapshot())
                .build();
    }

    public static Game fromSnapshot(final GameSnapshot gameSnapshot) {
        final Board board = Board.fromSnapshot(gameSnapshot.getBoardSnapshot());
        return Game.builder()
                .player1(new Player(gameSnapshot.getPlayer1Id()))
                .player2(new Player(gameSnapshot.getPlayer2Id()))
                .board(board)
                .build();
    }
}
