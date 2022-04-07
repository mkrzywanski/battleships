package io.mkrzywanski.battleships.game.infra;

import io.mkrzywanski.battleships.game.domain.Game;
import io.mkrzywanski.battleships.game.domain.GameId;
import io.mkrzywanski.battleships.game.domain.GameRepository;
import io.mkrzywanski.battleships.game.domain.readmodel.GameView;
import io.mkrzywanski.battleships.game.domain.readmodel.ViewMappings;
import io.mkrzywanski.battleships.game.domain.snapshot.GameSnapshot;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
class InMemoryGameRepository implements GameRepository {

    private final Map<GameId, GameSnapshot> repository = new ConcurrentHashMap<>();

    @Override
    public void save(final Game game) {
        final GameSnapshot gameSnapshot = game.toSnapshot();
        repository.put(gameSnapshot.getGameId(), gameSnapshot);
    }

    @Override
    public Optional<GameView> findByGameId(final GameId gameId) {
        return Optional.ofNullable(repository.get(gameId))
                .map(ViewMappings::toView);
    }

}
