package com.github.ikhoury.twentyone.strategy;

import com.github.ikhoury.twentyone.deck.Card;
import com.github.ikhoury.twentyone.deck.Deck;
import com.github.ikhoury.twentyone.driver.InteractionDriver;
import org.junit.Before;
import org.mockito.Mock;

import java.util.Optional;

import static org.mockito.Mockito.when;

public abstract class AbstractTurnStrategyTest {

    static final Card NEXT_CARD = Card.EIGHT;
    static final Card CARD_WITH_MORE_THAN_ONE_POINTS = Card.ACE;
    static final int CHOSEN_POINTS = 5;
    static final int TURNS_TO_PLAY_AFTER_SPLIT = 2;

    @Mock
    Deck deck;
    @Mock
    InteractionDriver interactionDriver;

    @Before
    public void setUp() {
        when(deck.nextCard()).thenReturn(Optional.of(NEXT_CARD));
    }
}
