package com.github.ikhoury.twentyone.deck;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.shuffle;

public class Deck {

    private static final int NUMBER_OF_CARD_SUITS = 4;

    private final List<Card> cards;

    public Deck() {
        EnumSet<Card> allCards = EnumSet.allOf(Card.class);
        cards = new ArrayList<>(allCards.size() * NUMBER_OF_CARD_SUITS);

        for (int i = 0; i < NUMBER_OF_CARD_SUITS; i ++) {
            cards.addAll(allCards);
        }
    }

    public List<Card> getCards() {
        return cards;
    }

    public void shuffleCards() {
        shuffle(cards);
    }

    public Optional<Card> getNextCard() {
        if (cards.isEmpty()) {
            return Optional.empty();
        }

        Card lastCard = cards.remove(cards.size() - 1);
        return Optional.of(lastCard);
    }
}
