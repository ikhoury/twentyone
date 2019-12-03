package com.github.ikhoury.twentyone.engine;

import com.github.ikhoury.twentyone.deck.Deck;
import com.github.ikhoury.twentyone.driver.InteractionDriver;
import com.github.ikhoury.twentyone.driver.PlayerChoice;
import com.github.ikhoury.twentyone.player.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PlayerTurnStrategyTest extends AbstractTurnStrategyTest {

    @Mock
    private Deck deck;
    @Mock
    private InteractionDriver interactionDriver;
    @Mock
    private Player player;

    @InjectMocks
    private PlayerTurnStrategy strategy;

    @Before
    public void setUp() {
        when(deck.nextCard()).thenReturn(Optional.of(NEXT_CARD));
        when(interactionDriver.showCardAndChoosePoints(NEXT_CARD))
                .thenReturn(CHOSEN_POINTS);
    }

    @Test
    public void playerChoosesHit() {
        when(interactionDriver.askChoiceFrom(player))
                .thenReturn(PlayerChoice.HIT);

        strategy.playTurn(player);

        verify(player).hit(NEXT_CARD, CHOSEN_POINTS);
        verify(interactionDriver).showHitCardAndPoints(player, NEXT_CARD, CHOSEN_POINTS);
    }

    @Test
    public void playerChoosesHitOnEmptyDeckDoesNothing() {
        when(interactionDriver.askChoiceFrom(player))
                .thenReturn(PlayerChoice.HIT);
        when(deck.nextCard()).thenReturn(Optional.empty());

        strategy.playTurn(player);
    }

    @Test
    public void bustPlayerIfGetsBusted() {
        when(interactionDriver.askChoiceFrom(player))
                .thenReturn(PlayerChoice.HIT);

        strategy.playTurn(player);

        verify(interactionDriver).notifyIfBusted(player);
    }

    @Test
    public void playerChoosesStand() {
        when(interactionDriver.askChoiceFrom(player))
                .thenReturn(PlayerChoice.STAND);

        strategy.playTurn(player);

        verify(player).stand();
    }

    @Test
    public void playerChoosesSplitThenPlaysHitTurns() {
        when(interactionDriver.askChoiceFrom(player))
                .thenReturn(PlayerChoice.SPLIT, PlayerChoice.HIT);
        when(interactionDriver.chooseCardToSplitFor(player))
                .thenReturn(NEXT_CARD);
        when(player.canHit()).thenReturn(true);

        strategy.playTurn(player);

        verify(player).split(NEXT_CARD);
        verify(interactionDriver).showSplitCardAndPoints(player, NEXT_CARD);
        verify(interactionDriver, times(TURNS_TO_PLAY_AFTER_SPLIT + 1)).askChoiceFrom(player);
        verify(player, times(TURNS_TO_PLAY_AFTER_SPLIT)).hit(NEXT_CARD, CHOSEN_POINTS);
    }
}