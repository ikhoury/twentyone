package com.github.ikhoury.twentyone.strategy;

import com.github.ikhoury.twentyone.deck.Deck;
import com.github.ikhoury.twentyone.player.Player;

/**
 * Defines a strategy interface for playing a player's turn
 */
public interface TurnStrategy {

    void playTurn(Player player, Deck deck);
}
