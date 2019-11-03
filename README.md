# TwentyOne
[![CircleCI](https://circleci.com/gh/ikhoury/twentyone.svg?style=svg)](https://circleci.com/gh/ikhoury/twentyone)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ikhoury_twentyone&metric=alert_status)](https://sonarcloud.io/dashboard?id=ikhoury_twentyone)

A simple implementation of the Dutch variant of Blackjack.

## Game Rules
- Players place their bets after receiving their first card.
- When the round of bets finishes, players get their next card.
- Players play in turn.
- Players can choose 3 actions:
    - Stand: Hold total and stop playing.
    - Hit: Ask for a card.
    - Split: Choose two identical cards and remove them. The player then places two separate actions.
- Players can end their turn in two states:
    - Stand: Still playing against the bank with a total.
    - Bust: Lost because they have over 21 points.
- After all players end their turn, and there are players standing, the bank must play.
- The bank must _hit_ with a total of 16 points or less.
- The bank must _stand_ with a total of 17 points or more.
- If the bank and player have the same number of points, the bank wins.
- If the bank has more than 21 points, he is bust and all standing players win.
- Points are calculated as follows:
    - King is 3 points
    - Queen is 2 points
    - Jack is 1 point
    - Ace is 1 or 11 points of your choice
    - Cards 2 to 10 have their normal point value
- The suit of the card is not important
- The Joker does not play
- A total of three players can play with one card deck.

## Running the game
The game is a java application that is packaged into a runnable `JAR` using maven.
Clone the repository and then in the root directory run `mvn package`.
You will get a runnable `JAR` created under the `target/` folder ending with the prefix `jar-with-dependencies`.
Run the game using `java -jar`.