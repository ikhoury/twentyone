package com.github.ikhoury.twentyone;

import com.github.ikhoury.twentyone.deck.Deck;
import com.github.ikhoury.twentyone.driver.InteractionDriver;
import com.github.ikhoury.twentyone.engine.GameEngine;
import com.github.ikhoury.twentyone.player.Bank;
import com.github.ikhoury.twentyone.player.Player;
import com.github.ikhoury.twentyone.strategy.BankTurnStrategy;
import com.github.ikhoury.twentyone.strategy.PlayerTurnStrategy;
import com.github.ikhoury.twentyone.strategy.TurnStrategy;

import java.util.ArrayList;
import java.util.List;

import static com.github.ikhoury.twentyone.Constants.MAX_PLAYERS_PER_GAME;

class GameSplitter {

    private GameSplitter() {

    }

    static List<GameEngine> createGamesBasedOnNumberOfPlayers(InteractionDriver interactionDriver, List<Player> players) {
        List<GameEngine> gameEngines = new ArrayList<>();
        List<Player> playersChunk;

        TurnStrategy playerStrategy = new PlayerTurnStrategy(interactionDriver);
        TurnStrategy bankStrategy = new BankTurnStrategy(interactionDriver);

        while (!players.isEmpty()) {
            playersChunk = players.size() > MAX_PLAYERS_PER_GAME ? players.subList(0, MAX_PLAYERS_PER_GAME) : players;

            List<Player> currentPlayers = new ArrayList<>(playersChunk);
            Deck deck = new Deck();
            Bank bank = new Bank();

            GameEngine gameEngine = new GameEngine(deck, bank, currentPlayers, interactionDriver, playerStrategy, bankStrategy);

            gameEngines.add(gameEngine);
            playersChunk.clear();
        }

        return gameEngines;
    }
}
