//package io.mkrzywanski.battleships.infra;
//
//import io.mkrzywanski.battleships.domain.Game;
//import io.mkrzywanski.battleships.domain.GameId;
//import io.mkrzywanski.battleships.domain.GameRepository;
//import io.mkrzywanski.battleships.domain.view.GameSnapshot;
//
//import java.util.Map;
//import java.util.Optional;
//import java.util.concurrent.ConcurrentHashMap;
//
//class InMemoryGameRepository implements GameRepository {
//
//    private final Map<GameId, GameSnapshot> repository = new ConcurrentHashMap<>();
//
//    @Override
//    public void save(final Game game) {
//        final GameSnapshot gameSnapshot = game.toSnapshot();
//        repository.put(gameSnapshot.getGameId(), gameSnapshot);
//    }
//
//    @Override
//    public Optional<Game> findByGameId(final GameId gameId) {
//        return Optional.ofNullable(repository.get(gameId)).map(Game::fromSnapshot);
//    }
//}
