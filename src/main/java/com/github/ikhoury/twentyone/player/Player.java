package com.github.ikhoury.twentyone.player;

import com.github.ikhoury.twentyone.deck.Card;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private final String name;
    private List<Card> hand;
    private int points;
    private boolean bust;
    private boolean stand;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public int getPoints() {
        return points;
    }

    public boolean isBust() {
        return bust;
    }

    public boolean isStand() {
        return stand;
    }

    public void hit(Card card, int points) {
        this.hand.add(card);
        this.points += points;
    }

    public void stand() {
        this.stand = true;
    }

    public void bust() {
        this.bust = true;
    }
}
