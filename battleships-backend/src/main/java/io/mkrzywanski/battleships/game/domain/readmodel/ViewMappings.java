package io.mkrzywanski.battleships.game.domain.readmodel;

import io.mkrzywanski.battleships.game.domain.snapshot.BoardSnapshot;
import io.mkrzywanski.battleships.game.domain.snapshot.GameRulesSnapshot;
import io.mkrzywanski.battleships.game.domain.snapshot.GameSnapshot;
import io.mkrzywanski.battleships.game.domain.snapshot.PositionSnapshot;
import io.mkrzywanski.battleships.game.domain.snapshot.ShipCoordinateSnapshot;
import io.mkrzywanski.battleships.game.domain.snapshot.ShipSnapshot;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ViewMappings {

    public static GameView toView(final GameSnapshot gameSnapshot) {
        final var playersBoard = gameSnapshot.getBoardSnapshots()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey().uuid(), e -> toView(e.getValue())));
        final var snapshot = gameSnapshot.getBoardSnapshots().values().stream().findFirst().orElseThrow().getGameRules();
        final GameRulesView gameRulesView = toView(snapshot);
        return new GameView(
                gameSnapshot.getGameId().uuid(),
                gameSnapshot.getPlayer1Id().uuid(),
                gameSnapshot.getPlayer2Id().uuid(),
                gameSnapshot.getCurrentPlayer().uuid(),
                playersBoard,
                gameRulesView
        );
    }

    private static BoardView toView(final BoardSnapshot boardSnapshot) {
        final var shipViews = boardSnapshot.getShipSnapshots()
                .stream()
                .map(ViewMappings::toView).toList();
        final var additionalHits = boardSnapshot.getAdditionalHits()
                .stream()
                .map(position -> new PositionView(position.x(), position.y()))
                .collect(Collectors.toSet());
        return new BoardView(
                shipViews,
                additionalHits
        );
    }

    private static ShipView toView(final ShipSnapshot shipSnapshot) {
        final var shipCoordinateViews = shipSnapshot.getShipCoordinateSnapshotList()
                .stream()
                .map(ViewMappings::toView)
                .toList();
        return new ShipView(shipCoordinateViews);
    }

    private static ShipCoordinateView toView(final ShipCoordinateSnapshot snapshot) {
        final PositionSnapshot position = snapshot.getPosition();
        final PositionView positionView = new PositionView(position.getX(), position.getY());
        return new ShipCoordinateView(positionView, snapshot.isHit());
    }

    private static GameRulesView toView(final GameRulesSnapshot gameRulesSnapshot) {
        final var allowedShipDefinitions = gameRulesSnapshot.getAllowedShipDefinitions()
                .entrySet()
                .stream()
                .map(e -> new AllowedShipView(e.getValue(), e.getKey()))
                .toList();
        return new GameRulesView(
                gameRulesSnapshot.getBoardHeight(),
                gameRulesSnapshot.getBoardWidth(),
                allowedShipDefinitions
        );
    }
}
