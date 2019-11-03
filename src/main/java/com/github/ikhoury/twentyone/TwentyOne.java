package com.github.ikhoury.twentyone;

import com.github.ikhoury.twentyone.deck.Deck;
import com.github.ikhoury.twentyone.driver.ConsoleDriver;
import com.github.ikhoury.twentyone.driver.InteractionDriver;
import com.github.ikhoury.twentyone.engine.GameEngine;
import com.github.ikhoury.twentyone.engine.TurnEngine;
import com.github.ikhoury.twentyone.player.Bank;
import com.github.ikhoury.twentyone.player.Player;

import java.util.ArrayList;
import java.util.List;

import static com.github.ikhoury.twentyone.Constants.MAX_PLAYERS_PER_GAME;

public class TwentyOne {

    private static final InteractionDriver INTERACTION_DRIVER = new ConsoleDriver();

    public static void main(String[] args) {
        INTERACTION_DRIVER.greet();
        List<Player> players = INTERACTION_DRIVER.askForPlayerNames();
        List<GameEngine> gameEngines = createGamesBasedOnNumberOfPlayers(players);
        INTERACTION_DRIVER.announceGames(gameEngines);

        // Play each game in a row
        for (int i = 0; i < gameEngines.size(); i++) {
            INTERACTION_DRIVER.announceGameNumber(i + 1);
            gameEngines.get(i).run();
        }
    }

    private static List<GameEngine> createGamesBasedOnNumberOfPlayers(List<Player> players) {
        List<GameEngine> gameEngines = new ArrayList<>();
        List<Player> playersChunk;

        while (!players.isEmpty()) {
            playersChunk = players.size() > MAX_PLAYERS_PER_GAME ? players.subList(0, MAX_PLAYERS_PER_GAME) : players;

            List<Player> currentPlayers = new ArrayList<>(playersChunk);
            Deck deck = new Deck();
            Bank bank = new Bank();
            TurnEngine turnEngine = new TurnEngine(deck, INTERACTION_DRIVER);
            GameEngine gameEngine = new GameEngine(bank, currentPlayers, INTERACTION_DRIVER, turnEngine);

            gameEngines.add(gameEngine);
            playersChunk.clear();
        }

        return gameEngines;
    }
}
