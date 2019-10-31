package com.github.ikhoury.twentyone;

import com.github.ikhoury.twentyone.deck.Card;
import com.github.ikhoury.twentyone.deck.Deck;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class DeckTest {

    private static final int DECK_SIZE = 56;

    private Deck deck;
    private List<Card> deckCards;

    @Before
    public void setUp() {
        deck = new Deck();
        deckCards = deck.getCards();
    }

    @Test
    public void initializesDeck() {
        EnumSet<Card> allCards = EnumSet.allOf(Card.class);

        assertThat(deckCards, hasSize(DECK_SIZE));
        allCards.forEach(card -> assertThat(deckCards, hasItem(card)));
    }

    @Test
    public void shufflesDeck() {
        List<Card> beforeShuffle = new ArrayList<>(deckCards);

        deck.shuffleCards();

        assertThat(beforeShuffle, not(equalTo(deckCards)));
    }

    @Test
    public void removesFirstCardFromDeck() {
        Card expectedFirstCard = deckCards.get(deckCards.size() - 1);

        Card firstCard = deck.nextCard().orElse(null);

        assertThat(firstCard, equalTo(expectedFirstCard));
        assertThat(deckCards, hasSize(DECK_SIZE - 1));
    }
}