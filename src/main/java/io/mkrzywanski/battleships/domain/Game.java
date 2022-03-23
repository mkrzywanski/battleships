package io.mkrzywanski.battleships.domain;

import io.mkrzywanski.battleships.domain.view.GameSnapshot;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class Game {
    private final Player player1;
    private final Player player2;
    private final Board board;

    public boolean isFinished() {
        return getWinner().isPresent();
    }

    public Optional<Player> getWinner() {
        return board.hasAliveShipsFor(player1.playerId()) ? Optional.of(player1) : Optional.of(player2);
    }

    public boolean hit(Position position) {
        return board.hit(position);
    }

    public void placeShip(NewShip newShip, PlayerId playerId) {
        board.placeShip(newShip, playerId);
    }

    public GameSnapshot toSnapshot() {
        return GameSnapshot.builder()
                .player1Id(player1.playerId())
                .player2Id(player2.playerId())
                .boardSnapshot(board.toSnapshot())
                .build();
    }
}
