package io.mkrzywanski.battleships.game.domain;

import io.mkrzywanski.battleships.game.domain.snapshot.GameSnapshot;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Game {

    private final PlayerId player1Id;
    private final PlayerId player2Id;
    private final Map<PlayerId, Board> boards;
    private final GameId gameId;
    private PlayerId currentPlayer;

    public Game(final PlayerId player1, final PlayerId player2, final GameRules gameRules) {
        this.player1Id = player1;
        this.player2Id = player2;
        this.boards = new HashMap<>();
        this.boards.put(player1, new Board(gameRules));
        this.boards.put(player2, new Board(gameRules));
        this.gameId = GameId.random();
        this.currentPlayer = player1;
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

    /**
     *
     * @param position position on board to hit
     * @param playerId player id of player whose board should be hit
     * @return
     */
    public boolean hit(final Position position, final PlayerId playerId) {
        if (playerId.equals(currentPlayer)) {
            throw new IllegalArgumentException(format("PlayerId %s is not current player ", playerId));
        }
        final boolean hit = boards.get(playerId).hit(position);
        if (!hit) {
            swapPlayerTurns();
        }
        return hit;
    }

    private void swapPlayerTurns() {
        if (currentPlayer.equals(player1Id)) {
            currentPlayer = player2Id;
        } else {
            currentPlayer = player1Id;
        }
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
                .currentPlayer(currentPlayer)
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
                .currentPlayer(gameSnapshot.getCurrentPlayer())
                .build();
    }
}
