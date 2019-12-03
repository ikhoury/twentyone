package com.github.ikhoury.twentyone.driver;

import com.github.ikhoury.twentyone.deck.Card;
import com.github.ikhoury.twentyone.engine.GameEngine;
import com.github.ikhoury.twentyone.player.Player;

import java.util.Collection;
import java.util.List;

/**
 * This interfaces game interactions with the user.
 * This should be used when requesting information from the user or displaying game progress.
 */
public interface InteractionDriver {

    void greet();

    void announceGames(Collection<GameEngine> gameEngines);

    void announceGameNumber(int number);

    PlayerChoice askChoiceFrom(Player player);

    int showCardAndChoosePoints(Card card);

    void showHitCardAndPoints(Player player, Card card, int points);

    void showSplitCardAndPoints(Player player, Card card);

    void bust(Player player);

    void notifyIfBusted(Player player);

    void win(Player won, Player lost);

    void winAll(Player won);

    Card chooseCardToSplitFor(Player player);

    List<Player> askForPlayerNames();
}
