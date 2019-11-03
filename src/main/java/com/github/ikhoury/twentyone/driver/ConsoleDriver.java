package com.github.ikhoury.twentyone.driver;

import com.github.ikhoury.twentyone.deck.Card;
import com.github.ikhoury.twentyone.engine.GameEngine;
import com.github.ikhoury.twentyone.player.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import static com.github.ikhoury.twentyone.Constants.MAX_PLAYERS_PER_GAME;
import static java.lang.String.format;

public class ConsoleDriver implements InteractionDriver {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void greet() {
        printToConsole("Welcome to TwentyOne!");
    }

    @Override
    public void announceGames(Collection<GameEngine> gameEngines) {
        printToConsole(format("A maximum of %d can play per game.", MAX_PLAYERS_PER_GAME));
        printToConsole(format("Based on the number of players, there will be %d game(s) to play.", gameEngines.size()));
    }

    @Override
    public void announceGameNumber(int number) {
        printToConsole(format(">> Game %d will begin!", number));
    }

    @Override
    public List<Player> askForPlayerNames() {
        List<Player> players = new ArrayList<>();

        printToConsole("Please enter a comma-separated list of players who want to join.");
        String input = readFromConsole();

        for (String playerName : input.split(",")) {
            Player player = new Player(playerName);
            players.add(player);
        }

        return players;
    }

    @Override
    public PlayerChoice askChoiceFrom(Player player) {
        if (player.canSplit()) {
            printToConsole(format("> %s please choose HIT, STAND, or SPLIT [%d points]", player.getName(), player.getPoints()));
        } else {
            printToConsole(format("> %s please choose HIT or STAND [%d points]", player.getName(), player.getPoints()));
        }

        String input = readFromConsole();
        PlayerChoice playerChoice = parsePlayerChoice(input);

        if (playerChoice == PlayerChoice.SPLIT && !player.canSplit()) {
            invalidInput(input);
            return askChoiceFrom(player);
        }

        return playerChoice;
    }

    @Override
    public int showCardAndChoosePoints(Card card) {
        if (card.getPossiblePoints().size() > 1) {
            printToConsole(format("You got card %s with %s possible points.", card.name(), card.getPossiblePoints()));
            printToConsole("Please choose which points to add.");

            String input = readFromConsole();
            int points = parsePlayerPoints(input);

            if (!card.getPossiblePoints().contains(points)) {
                invalidInput(input);
                return showCardAndChoosePoints(card);
            }

            return points;

        }

        return card.getPossiblePoints().iterator().next();
    }

    @Override
    public void bust(Player player) {
        printToConsole(format("%s is bust with %d points!", player.getName(), player.getPoints()));
    }

    @Override
    public void winAll(Player player) {
        printToConsole(format("%s has won with %d points!", player.getName(), player.getPoints()));
    }

    @Override
    public void win(Player won, Player lost) {
        printToConsole(format("%s with %d points won %s with %d points!", won.getName(), won.getPoints(), lost.getName(), lost.getPoints()));
    }

    @Override
    public Card chooseCardToSplitFor(Player player) {
        printToConsole("Choose which card to split on");

        String input = readFromConsole();
        Card card = parsePlayerCard(input);

        if (!player.canSplitOn(card)) {
            invalidInput(input);
            return chooseCardToSplitFor(player);
        }

        return card;
    }

    @Override
    public void showHitCardAndPoints(Player player, Card card, int points) {
        printToConsole(format("%s hit card %s with %d points, he now has %d total points.", player.getName(), card.name(), points, player.getPoints()));
    }

    @Override
    public void showSplitCardAndPoints(Player player, Card card) {
        printToConsole(format("%s split card %s and now has %d points.", player.getName(), card.name(), player.getPoints()));
    }

    private void printToConsole(String output) {
        System.out.println(output);
    }

    private String readFromConsole() {
        return scanner.nextLine();
    }

    private void invalidInput(String input) {
        printToConsole(format("Invalid input: %s", input));
    }

    private Card parsePlayerCard(String input) {
        try {
            return Card.valueOf(input);
        } catch (IllegalArgumentException exc) {
            invalidInput(input);
            return parsePlayerCard(readFromConsole());
        }
    }

    private PlayerChoice parsePlayerChoice(String input) {
        try {
            return PlayerChoice.valueOf(input);
        } catch (IllegalArgumentException exc) {
            invalidInput(input);
            return parsePlayerChoice(readFromConsole());
        }
    }

    private int parsePlayerPoints(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException exc) {
            invalidInput(input);
            return parsePlayerPoints(readFromConsole());
        }
    }
}
