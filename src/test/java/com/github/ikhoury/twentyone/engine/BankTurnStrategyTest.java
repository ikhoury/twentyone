package com.github.ikhoury.twentyone.engine;

import com.github.ikhoury.twentyone.deck.Card;
import com.github.ikhoury.twentyone.player.Bank;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static com.github.ikhoury.twentyone.Constants.BUST_THRESHOLD;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BankTurnStrategyTest extends AbstractTurnStrategyTest {

    @Mock
    private Bank bank;

    @InjectMocks
    private BankTurnStrategy strategy;

    @Test
    public void bankWithZeroPointsHitsCardWithHighestPoints() {
        int expectedPoints = CARD_WITH_MORE_THAN_ONE_POINTS.getPossiblePoints()
                .stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);

        when(bank.getPoints()).thenReturn(0);
        when(deck.nextCard()).thenReturn(Optional.of(CARD_WITH_MORE_THAN_ONE_POINTS));

        strategy.playTurn(bank);

        verify(bank).hit(CARD_WITH_MORE_THAN_ONE_POINTS, expectedPoints);
    }

    @Test
    public void bankWithPointsHitsCardWithHigherNewSumLessThanBustThreshold() {
        int expectedPoints = CARD_WITH_MORE_THAN_ONE_POINTS.getMinimumPoints().orElse(0);

        when(bank.getPoints()).thenReturn(BUST_THRESHOLD - expectedPoints);
        when(deck.nextCard()).thenReturn(Optional.of(CARD_WITH_MORE_THAN_ONE_POINTS));

        strategy.playTurn(bank);

        verify(bank).hit(CARD_WITH_MORE_THAN_ONE_POINTS, expectedPoints);
    }

    @Test
    public void bankWithPointsAtBustThresholdStillHits() {
        when(bank.getPoints()).thenReturn(BUST_THRESHOLD);

        strategy.playTurn(bank);

        verify(bank).hit(any(Card.class), anyInt());
    }

    @Test
    public void bankWithPointsOverBustThresholdChoosesSmallestPoints() {
        int expectedPoints = CARD_WITH_MORE_THAN_ONE_POINTS.getMinimumPoints().orElse(0);

        when(bank.getPoints()).thenReturn(BUST_THRESHOLD + 1);
        when(deck.nextCard()).thenReturn(Optional.of(CARD_WITH_MORE_THAN_ONE_POINTS));

        strategy.playTurn(bank);

        verify(bank).hit(CARD_WITH_MORE_THAN_ONE_POINTS, expectedPoints);
    }

    @Test
    public void bustBankIfGetsBusted() {
        strategy.playTurn(bank);

        verify(interactionDriver).notifyIfBusted(bank);
    }
}