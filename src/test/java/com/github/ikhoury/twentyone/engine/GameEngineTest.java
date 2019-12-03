package com.github.ikhoury.twentyone.engine;

import com.github.ikhoury.twentyone.driver.InteractionDriver;
import com.github.ikhoury.twentyone.player.Bank;
import com.github.ikhoury.twentyone.player.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.github.ikhoury.twentyone.Constants.MAX_PLAYERS_PER_GAME;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GameEngineTest {

    private static final int PLAYER_POINTS = 10;

    @Mock
    private Player player;
    @Mock
    private Bank bank;
    @Mock
    private InteractionDriver interactionDriver;
    @Mock
    private TurnEngine turnEngine;
    @Mock
    private TurnStrategy strategy;

    private GameEngine gameEngine;

    @Before
    public void setUp() {
        List<Player> players = new ArrayList<>();
        players.add(player);

        gameEngine = new GameEngine(bank, players, interactionDriver, turnEngine, strategy, strategy);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotCreateGameMoreThanMaxPlayers() {
        new GameEngine(bank, createPlayersMoreThanAllowed(), interactionDriver, turnEngine, strategy, strategy);
    }

    @Test
    public void playsPlayerTurnWhileCanHit() {
        // Can hit two times then stops
        when(player.canHit()).thenReturn(true, true, false);

        gameEngine.run();

        verify(strategy, times(2)).playTurn(player);
    }

    @Test
    public void playsBankTurnWhileCanHitAndPlayersAreStanding() {
        // Can hit two times then stops
        when(bank.canHit()).thenReturn(true, true, false);
        when(player.isStanding()).thenReturn(true);

        gameEngine.run();

        verify(strategy, times(2)).playTurn(bank);
    }

    @Test
    public void bankWinsAllIfNoPlayerStands() {
        when(player.isStanding()).thenReturn(false);

        gameEngine.run();

        verify(turnEngine, never()).playTurn(bank);
        verify(interactionDriver).winAll(bank);
    }

    @Test
    public void playersWinIfBankIsBusted() {
        when(bank.isBusted()).thenReturn(true);
        when(player.isStanding()).thenReturn(true);

        gameEngine.run();

        verify(interactionDriver).win(player, bank);
    }

    @Test
    public void bankWinsPlayerIfEqualPoints() {
        when(bank.getPoints()).thenReturn(PLAYER_POINTS);
        when(player.getPoints()).thenReturn(PLAYER_POINTS);
        when(player.isStanding()).thenReturn(true);
        when(bank.isBusted()).thenReturn(false);

        gameEngine.run();

        verify(interactionDriver).win(bank, player);
    }

    @Test
    public void bankWinsIfGreaterThanPlayerPoints() {
        when(bank.getPoints()).thenReturn(PLAYER_POINTS);
        when(player.getPoints()).thenReturn(PLAYER_POINTS - 2);
        when(player.isStanding()).thenReturn(true);
        when(bank.isBusted()).thenReturn(false);

        gameEngine.run();

        verify(interactionDriver).win(bank, player);
    }

    @Test
    public void bankLosesIfLessThanPlayerPoints() {
        when(bank.getPoints()).thenReturn(PLAYER_POINTS - 1);
        when(player.getPoints()).thenReturn(PLAYER_POINTS);
        when(player.isStanding()).thenReturn(true);
        when(bank.isBusted()).thenReturn(false);

        gameEngine.run();

        verify(interactionDriver).win(player, bank);
    }

    private Collection<Player> createPlayersMoreThanAllowed() {
        int size = MAX_PLAYERS_PER_GAME + 1;
        List<Player> players = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            players.add(player);
        }

        return players;
    }
}