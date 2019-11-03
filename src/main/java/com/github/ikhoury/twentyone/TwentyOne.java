package com.github.ikhoury.twentyone;

import com.github.ikhoury.twentyone.driver.ConsoleDriver;
import com.github.ikhoury.twentyone.driver.InteractionDriver;
import com.github.ikhoury.twentyone.engine.GameEngine;
import com.github.ikhoury.twentyone.player.Player;

import java.util.List;

import static com.github.ikhoury.twentyone.GameSplitter.createGamesBasedOnNumberOfPlayers;

public class TwentyOne {

    public static void main(String[] args) {
        InteractionDriver interactionDriver = new ConsoleDriver();

        interactionDriver.greet();
        List<Player> players = interactionDriver.askForPlayerNames();

        List<GameEngine> gameEngines = createGamesBasedOnNumberOfPlayers(interactionDriver, players);
        interactionDriver.announceGames(gameEngines);

        // Play each game in a row
        for (int i = 0; i < gameEngines.size(); i++) {
            interactionDriver.announceGameNumber(i + 1);
            gameEngines.get(i).run();
        }
    }
}
