package com.github.ikhoury.twentyone.player;

import com.github.ikhoury.twentyone.deck.Card;
import org.junit.Before;
import org.junit.Test;

import static com.github.ikhoury.twentyone.Constants.BUST_THRESHOLD;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class PlayerTest {

    private static final String PLAYER_NAME = "John Doe";
    private static final int INITIAL_PLAYER_POINTS = 0;
    private static final int BUST_POINTS = BUST_THRESHOLD + 1;
    private static final Card ACE_CARD = Card.ACE;

    private Player player;

    @Before
    public void setUp() {
        player = new Player(PLAYER_NAME);
    }

    @Test
    public void createsNewPlayer() {
        assertThat(player.getName(), equalTo(PLAYER_NAME));
        assertThat(player.getCards(), is(empty()));
        assertThat(player.getPoints(), equalTo(INITIAL_PLAYER_POINTS));
    }

    @Test
    public void hitAddsCardAndPoints() {
        int points = 8;

        player.hit(ACE_CARD, points);

        assertThat(player.getCards(), hasItem(ACE_CARD));
        assertThat(player.getPoints(), equalTo(INITIAL_PLAYER_POINTS + points));
    }

    @Test
    public void stands() {
        player.stand();

        assertThat(player.isStand(), is(true));
    }

    @Test
    public void bustsWhenOverLimit() {
        player.hit(ACE_CARD, BUST_POINTS);

        assertThat(player.isBust(), is(true));
    }

    @Test
    public void canHitIfNotBustOrStand() {
        assertThat(player.canHit(), is(true));
    }

    @Test
    public void cannotHitIfBust() {
        player.hit(ACE_CARD, BUST_POINTS);

        assertThat(player.canHit(), is(false));
    }

    @Test
    public void cannotHitIfStand() {
        player.stand();

        assertThat(player.canHit(), is(false));
    }

    @Test
    public void canSplitIfHasDuplicateCards() {
        player.hit(ACE_CARD, 1);
        player.hit(ACE_CARD, 1);

        assertThat(player.canSplit(), is(true));
        assertThat(player.canSplitOn(ACE_CARD), is(true));
    }

    @Test
    public void cannotSplitIfDoesNotHaveDuplicateCards() {
        player.hit(ACE_CARD, 1);

        assertThat(player.canSplit(), is(false));
        assertThat(player.canSplitOn(ACE_CARD), is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void splitOnNonDuplicateCardThrowsException() {
        player.hit(ACE_CARD, 1);
        player.hit(ACE_CARD, 1);
        player.hit(Card.EIGHT, 1);

        player.split(Card.EIGHT);
    }

    @Test
    public void splitOnDuplicateCardRemovesTwoCards() {
        int duplicateCardPoints = 3;
        Card otherCard = Card.EIGHT;
        player.hit(ACE_CARD, duplicateCardPoints);
        player.hit(ACE_CARD, duplicateCardPoints);
        player.hit(ACE_CARD, duplicateCardPoints);
        player.hit(otherCard, 5);
        int previousPoints = player.getPoints();

        player.split(ACE_CARD);

        int expectedPoints = previousPoints - 2 * duplicateCardPoints;
        assertThat(player.getPoints(), equalTo(expectedPoints));
        assertThat(player.getCards(), containsInAnyOrder(ACE_CARD, otherCard));
    }
}