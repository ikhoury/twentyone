package com.github.ikhoury.twentyone.engine;

import com.github.ikhoury.twentyone.deck.Card;
import com.github.ikhoury.twentyone.deck.Deck;
import com.github.ikhoury.twentyone.driver.InteractionDriver;
import com.github.ikhoury.twentyone.driver.PlayerChoice;
import com.github.ikhoury.twentyone.player.Bank;
import com.github.ikhoury.twentyone.player.Player;

import static com.github.ikhoury.twentyone.Constants.AVAILABLE_TURNS_AFTER_SPLIT;
import static com.github.ikhoury.twentyone.Constants.BUST_THRESHOLD;

public class TurnEngine {

    private final Deck deck;
    private final InteractionDriver interactionDriver;

    public TurnEngine(Deck deck, InteractionDriver interactionDriver) {
        this.deck = deck;
        this.interactionDriver = interactionDriver;
    }

    void playTurn(Bank bank) {
        deck.nextCard().ifPresent(card -> playHitWith(bank, card));
        notifyIfBusted(bank);
    }

    void playTurn(Player player) {
        PlayerChoice choice = interactionDriver.askForChoiceFrom(player);

        switch (choice) {
            case HIT:
                deck.nextCard().ifPresent(card -> playHitWith(player, card));
                break;
            case STAND:
                player.stand();
                break;
            case SPLIT:
                Card splitCard = interactionDriver.chooseCardToSplitFrom(player);
                playSplitWith(player, splitCard);
                break;
        }

        notifyIfBusted(player);
    }

    void competeWithBank(Player player, Bank bank) {
        if (player.getPoints() > bank.getPoints()) {
            interactionDriver.win(player);
        } else {
            interactionDriver.win(bank);
        }
    }

    private void playHitWith(Bank bank, Card card) {
        Integer pointsToAdd = null;
        int currentPoints = bank.getPoints();

        for (Integer possiblePoints : card.getPossiblePoints()) {
            int sum = currentPoints + possiblePoints;

            if (sum <= BUST_THRESHOLD && (pointsToAdd == null || sum > pointsToAdd)) {
                pointsToAdd = possiblePoints;
            }
        }

        if (pointsToAdd != null) {
            bank.hit(card, pointsToAdd);
            interactionDriver.showBankHitCard(bank, card, pointsToAdd);
        }
    }

    private void playHitWith(Player player, Card card) {
        int points = interactionDriver.showCardAndChoosePoints(card);
        player.hit(card, points);
    }

    private void playSplitWith(Player player, Card card) {
        player.split(card);

        for (int i = 0; i < AVAILABLE_TURNS_AFTER_SPLIT; i++) {
            playTurn(player);

            if (!player.canHit()) {
                break;
            }
        }
    }

    private void notifyIfBusted(Player player) {
        if (player.isBusted()) {
            interactionDriver.bust(player);
        }
    }
}
