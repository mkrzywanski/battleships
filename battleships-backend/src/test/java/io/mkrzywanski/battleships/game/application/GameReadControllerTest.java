package io.mkrzywanski.battleships.game.application;

import io.mkrzywanski.battleships.data.GameBuilder;
import io.mkrzywanski.battleships.game.domain.Game;
import io.mkrzywanski.battleships.game.domain.GameRepository;
import io.mkrzywanski.battleships.game.domain.snapshot.GameSnapshot;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import static io.mkrzywanski.battleships.spec.GameBodySpecAssertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GameReadControllerTest {

    @Autowired
    private GameRepository gameRepository;

    private WebTestClient webTestClient;

    @LocalServerPort
    private int port;

    @BeforeAll
    void beforeAll() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void shouldReturnBoardById() {
        final Game game = GameBuilder.newGame()
                .build();
        gameRepository.save(game);

        final GameSnapshot gameSnapshot = game.toSnapshot();

        final var gameViewBody = webTestClient.get()
                .uri("/games/" + gameSnapshot.getGameId().uuid())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody();

        assertThat(gameViewBody)
                .hasGameId(gameSnapshot.getGameId())
                .hasBoardForPlayer(gameSnapshot.getPlayer1Id())
                    .withEmptyShips()
                    .check()
                .hasBoardForPlayer(gameSnapshot.getPlayer2Id())
                    .withEmptyShips()
                    .check()
                .hasGameRules()
                    .withWidth(4)
                    .withHeight(4)
                    .withAllowedShipCount(1, 3)
                    .check();

    }
}
