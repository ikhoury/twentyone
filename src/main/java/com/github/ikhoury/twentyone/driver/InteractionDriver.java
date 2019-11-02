package com.github.ikhoury.twentyone.driver;

import com.github.ikhoury.twentyone.deck.Card;
import com.github.ikhoury.twentyone.player.Player;

/**
 * This interfaces user game interactions with the user.
 * This should be used when requesting information from the user or displaying game progress.
 */
public interface InteractionDriver {

    PlayerChoice askForChoiceFrom(Player player);

    int showCardAndChoosePoints(Card card);

    void showHitCardAndPoints(Player player, Card card, int points);

    void showSplitCardAndPoints(Player player, Card card);

    void bust(Player player);

    void win(Player won, Player lost);

    void winAll(Player won);

    Card chooseCardToSplitFrom(Player player);
}
