package com.github.ikhoury.twentyone.strategy;

import com.github.ikhoury.twentyone.deck.Card;
import com.github.ikhoury.twentyone.deck.Deck;
import com.github.ikhoury.twentyone.driver.InteractionDriver;
import com.github.ikhoury.twentyone.player.Player;

import java.util.Optional;
import java.util.OptionalInt;

import static com.github.ikhoury.twentyone.Constants.BUST_THRESHOLD;

public class BankTurnStrategy implements TurnStrategy {

    private final InteractionDriver interactionDriver;

    public BankTurnStrategy(InteractionDriver interactionDriver) {
        this.interactionDriver = interactionDriver;
    }

    @Override
    public void playTurn(Player player, Deck deck) {
        deck.nextCard().ifPresent(card -> hit(player, card));
    }

    private void hit(Player player, Card card) {
        OptionalInt pointsToAddOpt = findBestPossiblePointsThatWontBust(player, card)
                .map(OptionalInt::of)
                .orElse(card.getMinimumPoints());

        pointsToAddOpt.ifPresent(pointsToAdd -> {
            player.hit(card, pointsToAdd);
            interactionDriver.showHitCardAndPoints(player, card, pointsToAdd);
        });
    }

    private Optional<Integer> findBestPossiblePointsThatWontBust(Player player, Card card) {
        Integer pointsToAdd = null;
        int currentPoints = player.getPoints();

        for (Integer possiblePoints : card.getPossiblePoints()) {
            int sum = currentPoints + possiblePoints;

            if (sum <= BUST_THRESHOLD && (pointsToAdd == null || sum > pointsToAdd)) {
                pointsToAdd = possiblePoints;
            }
        }

        return Optional.ofNullable(pointsToAdd);
    }
}
