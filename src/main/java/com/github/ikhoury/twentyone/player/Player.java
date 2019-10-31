package com.github.ikhoury.twentyone.player;

import com.github.ikhoury.twentyone.deck.Card;

import java.util.*;

import static com.github.ikhoury.twentyone.Constants.BUST_THRESHOLD;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class Player {

    private final String name;
    private List<CardHit> hits;
    private boolean stand;

    public Player(String name) {
        this.name = name;
        this.hits = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Card> getCards() {
        return hits.stream()
                .map(CardHit::getCard)
                .collect(toList());
    }

    public int getPoints() {
        return hits.stream()
                .mapToInt(CardHit::getPoints)
                .sum();
    }

    public boolean isBust() {
        return getPoints() > BUST_THRESHOLD;
    }

    public boolean isStand() {
        return stand;
    }

    public boolean canHit() {
        return !isBust() && !isStand();
    }

    public boolean canSplit() {
        Set<Card> seenCards = new HashSet<>(hits.size());

        for (Card currentCard : getCards()) {
            if (seenCards.contains(currentCard)) {
                return true;
            }

            seenCards.add(currentCard);
        }

        return false;
    }

    public boolean canSplitOn(Card card) {
        Set<Card> seenCards = new HashSet<>(hits.size());

        for (Card currentCard : getCards()) {
            if (seenCards.contains(currentCard) && currentCard == card) {
                return true;
            }

            seenCards.add(currentCard);
        }

        return false;
    }

    public void hit(Card card, int points) {
        this.hits.add(new CardHit(card, points));
    }

    public void stand() {
        this.stand = true;
    }

    public void split(Card card) {
        if (!canSplitOn(card)) {
            throw new IllegalArgumentException(format("Card %s is not a candidate for splitting", card.name()));
        }

        int removed = 0;
        Iterator<CardHit> handIterator = hits.iterator();
        while (handIterator.hasNext()) {
            if (removed == 2) {
                break;
            }

            if (handIterator.next().getCard() == card) {
                handIterator.remove();
                removed++;
            }
        }
    }

    private static class CardHit {

        private Card card;
        private int points;

        CardHit(Card card, int points) {
            this.card = card;
            this.points = points;
        }

        Card getCard() {
            return card;
        }

        int getPoints() {
            return points;
        }
    }
}
