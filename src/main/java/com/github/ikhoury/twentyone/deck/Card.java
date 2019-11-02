package com.github.ikhoury.twentyone.deck;

import java.util.Collection;
import java.util.OptionalInt;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

/**
 * Represents the possible instances of a card in the game along with
 * the number of points it can give the player
 */
public enum Card {

    ACE(asList(1, 11)),
    ONE(singletonList(1)),
    TWO(singletonList(2)),
    THREE(singletonList(3)),
    FOUR(singletonList(4)),
    FIVE(singletonList(5)),
    SIX(singletonList(6)),
    SEVEN(singletonList(7)),
    EIGHT(singletonList(8)),
    NINE(singletonList(9)),
    TEN(singletonList(10)),
    JACK(singletonList(1)),
    QUEEN(singletonList(2)),
    KING(singletonList(3));

    private final Collection<Integer> possiblePoints;

    Card(Collection<Integer> possiblePoints) {
        this.possiblePoints = possiblePoints;
    }

    public Collection<Integer> getPossiblePoints() {
        return possiblePoints;
    }

    public OptionalInt getMinimumPoints() {
        return getPossiblePoints().stream()
                .mapToInt(Integer::intValue)
                .min();
    }
}
