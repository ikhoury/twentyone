package com.github.ikhoury.twentyone.engine;

import com.github.ikhoury.twentyone.driver.InteractionDriver;
import com.github.ikhoury.twentyone.player.Bank;
import com.github.ikhoury.twentyone.player.Player;

import java.util.Collection;
import java.util.function.Predicate;

import static com.github.ikhoury.twentyone.Constants.MAX_PLAYERS_PER_GAME;
import static java.util.stream.Collectors.toList;

/**
 * A game encapsulates a bank, a deck of cards, and a list of players competing against each other and the bank.
 * The game engine runs the main event loop and ends the game by choosing the winners.
 * A limited number of players is allowed per game.
 */
public class GameEngine implements Runnable {

    private static final Predicate<Player> STILL_STANDING = Player::isStanding;
    private static final Predicate<Player> STILL_PLAYING = Player::canHit;

    private final Bank bank;
    private final Collection<Player> players;
    private final InteractionDriver interactionDriver;
    private final TurnEngine turnEngine;

    private final TurnStrategy playerStrategy;
    private final TurnStrategy bankStrategy;

    public GameEngine(Bank bank, Collection<Player> players, InteractionDriver interactionDriver, TurnEngine turnEngine,
                      TurnStrategy playerStrategy, TurnStrategy bankStrategy) {
        if (players.size() > MAX_PLAYERS_PER_GAME) {
            throw new IllegalArgumentException("Number of players exceeds " + MAX_PLAYERS_PER_GAME);
        }

        this.bank = bank;
        this.players = players;
        this.interactionDriver = interactionDriver;
        this.turnEngine = turnEngine;
        this.playerStrategy = playerStrategy;
        this.bankStrategy = bankStrategy;
    }

    @Override
    public void run() {
        playUntilNoPlayersCanHit();

        Collection<Player> standingPlayers = findPlayersThatAre(STILL_STANDING);

        if (standingPlayers.isEmpty()) {
            interactionDriver.winAll(bank);
            return;
        }

        playBankTurn();
        findWinners(standingPlayers);
    }

    private void playUntilNoPlayersCanHit() {
        Collection<Player> playing;

        do {
            playing = findPlayersThatAre(STILL_PLAYING);
            playing.forEach(turnEngine::playTurn);
        } while (!playing.isEmpty());
    }

    private void playBankTurn() {
        while (bank.canHit()) {
            bankStrategy.playTurn(bank);
        }
    }

    private void findWinners(Collection<Player> standingPlayers) {
        if (bank.isBusted()) {
            standingPlayers.forEach(player -> interactionDriver.win(player, bank));
        } else {
            standingPlayers.forEach(player -> competeWithBank(player, bank));
        }
    }

    private void competeWithBank(Player player, Bank bank) {
        if (player.getPoints() > bank.getPoints()) {
            interactionDriver.win(player, bank);
        } else {
            interactionDriver.win(bank, player);
        }
    }

    private Collection<Player> findPlayersThatAre(Predicate<Player> criteria) {
        return players.stream()
                .filter(criteria)
                .collect(toList());
    }
}
