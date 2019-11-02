package com.github.ikhoury.twentyone.engine;

import com.github.ikhoury.twentyone.deck.Card;
import com.github.ikhoury.twentyone.deck.Deck;
import com.github.ikhoury.twentyone.driver.InteractionDriver;
import com.github.ikhoury.twentyone.driver.PlayerChoice;
import com.github.ikhoury.twentyone.player.Bank;
import com.github.ikhoury.twentyone.player.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static com.github.ikhoury.twentyone.Constants.BUST_THRESHOLD;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TurnEngineTest {

    private static final Card NEXT_CARD = Card.EIGHT;
    private static final Card CARD_WITH_MORE_THAN_ONE_POINTS = Card.ACE;
    private static final int CHOSEN_POINTS = 5;
    private static final int TURNS_TO_PLAY_AFTER_SPLIT = 2;

    @Mock private Deck deck;
    @Mock private InteractionDriver interactionDriver;
    @Mock private Player player;
    @Mock private Bank bank;

    @InjectMocks private TurnEngine turnEngine;

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

        turnEngine.playTurn(player);

        verify(player).hit(NEXT_CARD, CHOSEN_POINTS);
        verify(interactionDriver).showHitCardAndPoints(player, NEXT_CARD, CHOSEN_POINTS);
    }

    @Test
    public void bustPlayerIfGetsBusted() {
        when(interactionDriver.askChoiceFrom(player))
                .thenReturn(PlayerChoice.HIT);
        when(player.isBusted()).thenReturn(true);

        turnEngine.playTurn(player);

        verify(interactionDriver).bust(player);
    }

    @Test
    public void playerChoosesStand() {
        when(interactionDriver.askChoiceFrom(player))
                .thenReturn(PlayerChoice.STAND);

        turnEngine.playTurn(player);

        verify(player).stand();
    }

    @Test
    public void playerChoosesSplitThenPlaysHitTurns() {
        when(interactionDriver.askChoiceFrom(player))
                .thenReturn(PlayerChoice.SPLIT, PlayerChoice.HIT);
        when(interactionDriver.chooseCardToSplitFor(player))
                .thenReturn(NEXT_CARD);
        when(player.canHit()).thenReturn(true);

        turnEngine.playTurn(player);

        verify(player).split(NEXT_CARD);
        verify(interactionDriver).showSplitCardAndPoints(player, NEXT_CARD);
        verify(interactionDriver, times(TURNS_TO_PLAY_AFTER_SPLIT + 1)).askChoiceFrom(player);
        verify(player, times(TURNS_TO_PLAY_AFTER_SPLIT)).hit(NEXT_CARD, CHOSEN_POINTS);
    }

    @Test
    public void bankWithZeroPointsHitsCardWithHighestPoints() {
        int expectedPoints = CARD_WITH_MORE_THAN_ONE_POINTS.getPossiblePoints()
                .stream()
                .mapToInt(Integer::intValue)
                .max()
                .getAsInt();

        when(bank.getPoints()).thenReturn(0);
        when(deck.nextCard()).thenReturn(Optional.of(CARD_WITH_MORE_THAN_ONE_POINTS));

        turnEngine.playTurn(bank);

        verify(bank).hit(CARD_WITH_MORE_THAN_ONE_POINTS, expectedPoints);
    }

    @Test
    public void bankWithPointsHitsCardWithHighestNewSumLessThanBustThreshold() {
        int expectedPoints = CARD_WITH_MORE_THAN_ONE_POINTS.getPossiblePoints()
                .stream()
                .mapToInt(Integer::intValue)
                .min()
                .getAsInt();

        when(bank.getPoints()).thenReturn(BUST_THRESHOLD - expectedPoints);
        when(deck.nextCard()).thenReturn(Optional.of(CARD_WITH_MORE_THAN_ONE_POINTS));

        turnEngine.playTurn(bank);

        verify(bank).hit(CARD_WITH_MORE_THAN_ONE_POINTS, expectedPoints);
    }

    @Test
    public void bankWithPointsAtBustThresholdStillHits() {
        when(bank.getPoints()).thenReturn(BUST_THRESHOLD);

        turnEngine.playTurn(bank);

        verify(bank).hit(any(Card.class), anyInt());
    }

    @Test
    public void bustBankIfGetsBusted() {
        when(bank.isBusted()).thenReturn(true);

        turnEngine.playTurn(bank);

        verify(interactionDriver).bust(bank);
    }
}