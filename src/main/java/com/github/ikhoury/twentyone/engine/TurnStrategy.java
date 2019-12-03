package com.github.ikhoury.twentyone.engine;

import com.github.ikhoury.twentyone.player.Player;

/**
 * Defines a strategy interface for playing a player's turn
 */
public interface TurnStrategy {

    void playTurn(Player player);
}
