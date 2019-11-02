package com.github.ikhoury.twentyone.driver;

import com.github.ikhoury.twentyone.deck.Card;
import com.github.ikhoury.twentyone.player.Bank;
import com.github.ikhoury.twentyone.player.Player;

public interface InteractionDriver {

    PlayerChoice askForChoiceFrom(Player player);

    int showCardAndChoosePoints(Card card);

    void showBankHitCard(Bank bank, Card card, int points);

    void bust(Player player);

    void win(Player player);

    Card chooseCardToSplitFrom(Player player);
}
