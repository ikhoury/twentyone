package com.github.ikhoury.twentyone.strategy;

import com.github.ikhoury.twentyone.deck.Card;
import com.github.ikhoury.twentyone.deck.Deck;
import com.github.ikhoury.twentyone.driver.InteractionDriver;
import com.github.ikhoury.twentyone.driver.PlayerChoice;
import com.github.ikhoury.twentyone.player.Player;

import static com.github.ikhoury.twentyone.Constants.AVAILABLE_TURNS_AFTER_SPLIT;

public class PlayerTurnStrategy implements TurnStrategy {

    private final InteractionDriver interactionDriver;

    public PlayerTurnStrategy(InteractionDriver interactionDriver) {
        this.interactionDriver = interactionDriver;
    }

    @Override
    public void playTurn(Player player, Deck deck) {
        PlayerChoice choice = interactionDriver.askChoiceFrom(player);

        switch (choice) {
            case HIT:
                deck.nextCard().ifPresent(card -> hit(player, card));
                break;
            case STAND:
                player.stand();
                break;
            case SPLIT:
                Card splitCard = interactionDriver.chooseCardToSplitFor(player);
                split(deck, player, splitCard);
                break;
        }
    }

    private void hit(Player player, Card card) {
        int points = interactionDriver.showCardAndChoosePoints(card);
        player.hit(card, points);
        interactionDriver.showHitCardAndPoints(player, card, points);
    }

    private void split(Deck deck, Player player, Card card) {
        player.split(card);
        interactionDriver.showSplitCardAndPoints(player, card);

        for (int i = 0; i < AVAILABLE_TURNS_AFTER_SPLIT; i++) {
            playTurn(player, deck);

            if (!player.canHit()) {
                break;
            }
        }
    }
}
