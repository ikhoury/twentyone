package com.github.ikhoury.twentyone;

import com.github.ikhoury.twentyone.deck.Deck;
import com.github.ikhoury.twentyone.driver.InteractionDriver;
import com.github.ikhoury.twentyone.engine.GameEngine;
import com.github.ikhoury.twentyone.engine.TurnEngine;
import com.github.ikhoury.twentyone.player.Bank;
import com.github.ikhoury.twentyone.player.Player;

import java.util.ArrayList;
import java.util.List;

import static com.github.ikhoury.twentyone.Constants.MAX_PLAYERS_PER_GAME;

class GameSplitter {

    private GameSplitter() {

    }

    static List<GameEngine> createGamesBasedOnNumberOfPlayers(InteractionDriver interactionDriver, List<Player> players) {
        List<GameEngine> gameEngines = new ArrayList<>();
        List<Player> playersChunk;

        while (!players.isEmpty()) {
            playersChunk = players.size() > MAX_PLAYERS_PER_GAME ? players.subList(0, MAX_PLAYERS_PER_GAME) : players;

            List<Player> currentPlayers = new ArrayList<>(playersChunk);
            Deck deck = new Deck();
            Bank bank = new Bank();
            TurnEngine turnEngine = new TurnEngine(deck, interactionDriver);
            GameEngine gameEngine = new GameEngine(bank, currentPlayers, interactionDriver, turnEngine);

            gameEngines.add(gameEngine);
            playersChunk.clear();
        }

        return gameEngines;
    }
}