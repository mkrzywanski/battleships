package io.mkrzywanski.battleships.domain;

import io.mkrzywanski.battleships.domain.view.GameSnapshot;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Game {

    private final PlayerId player1Id;
    private final PlayerId player2Id;
    private final Map<PlayerId, Board> boards;
    private final GameId gameId;

    public Game(final PlayerId player1, final PlayerId player2, final GameRules gameRules) {
        this.player1Id = player1;
        this.player2Id = player2;
        this.boards = new HashMap<>();
        this.boards.put(player1, new Board(gameRules));
        this.boards.put(player2, new Board(gameRules));
        this.gameId = GameId.random();
    }

    public boolean isFinished() {
        return getWinner().isPresent();
    }

    public Optional<PlayerId> getWinner() {
        return boards.entrySet()
                .stream()
                .filter(e -> !e.getValue().hasNoAliveShips())
                .map(Map.Entry::getKey)
                .findAny();
    }

    public boolean hit(final Position position, final PlayerId playerId) {
        return boards.get(playerId).hit(position);
    }

    public void placeShip(final NewShip newShip, final PlayerId playerId) {
        boards.get(playerId).placeShip(newShip);
    }

    public boolean canBeStarted() {
        return boards.size() == 2 && boards.entrySet().stream().allMatch(e -> e.getValue().hasEnoughShips());
    }

    public GameSnapshot toSnapshot() {
        final var collect = boards.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toSnapshot()));
        return GameSnapshot.builder()
                .player1Id(player1Id)
                .player2Id(player2Id)
                .gameId(gameId)
                .boardSnapshots(collect)
                .build();
    }

    public static Game fromSnapshot(final GameSnapshot gameSnapshot) {
        final var playerToBoard = gameSnapshot.getBoardSnapshots()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> Board.fromSnapshot(e.getValue())));
        return Game.builder()
                .player1Id(gameSnapshot.getPlayer1Id())
                .player2Id(gameSnapshot.getPlayer2Id())
                .gameId(gameSnapshot.getGameId())
                .boards(playerToBoard)
                .build();
    }
}
