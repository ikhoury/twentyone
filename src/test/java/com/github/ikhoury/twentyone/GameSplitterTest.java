package com.github.ikhoury.twentyone;

import com.github.ikhoury.twentyone.driver.InteractionDriver;
import com.github.ikhoury.twentyone.engine.GameEngine;
import com.github.ikhoury.twentyone.player.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static com.github.ikhoury.twentyone.Constants.MAX_PLAYERS_PER_GAME;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class GameSplitterTest {

    @Mock private Player player;
    @Mock private InteractionDriver interactionDriver;

    @Test
    public void createsGamesBasedOnMaxAllowedPlayers() {
        int expectedNumberOfGames = 3;
        int playersToAdd = MAX_PLAYERS_PER_GAME * expectedNumberOfGames;
        List<Player> players = createPlayers(playersToAdd);

        List<GameEngine> games = GameSplitter.createGamesBasedOnNumberOfPlayers(interactionDriver, players);

        assertThat(games, hasSize(expectedNumberOfGames));
    }

    private List<Player> createPlayers(int numberOfPlayers) {
        List<Player> players = new ArrayList<>(numberOfPlayers);

        for (int i = 0; i < numberOfPlayers; i++) {
            players.add(player);
        }

        return players;
    }
}