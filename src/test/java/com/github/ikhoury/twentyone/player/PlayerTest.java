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

    private Player player;

    @Before
    public void setUp() {
        player = new Player(PLAYER_NAME);
    }

    @Test
    public void createsNewPlayer() {
        assertThat(player.getName(), equalTo(PLAYER_NAME));
        assertThat(player.getHand(), is(empty()));
        assertThat(player.getPoints(), equalTo(INITIAL_PLAYER_POINTS));
    }

    @Test
    public void hitAddsCardAndPoints() {
        Card card = Card.ACE;
        int points = 8;

        player.hit(card, points);

        assertThat(player.getHand(), hasItem(card));
        assertThat(player.getPoints(), equalTo(INITIAL_PLAYER_POINTS + points));
    }

    @Test
    public void stands() {
        player.stand();

        assertThat(player.isStand(), is(true));
    }

    @Test
    public void bustsWhenOverLimit() {
        player.hit(Card.ACE, BUST_POINTS);

        assertThat(player.isBust(), is(true));
    }

    @Test
    public void canHitIfNotBustOrStand() {
        assertThat(player.canHit(), is(true));
    }

    @Test
    public void cannotHitIfBust() {
        player.hit(Card.ACE, BUST_POINTS);

        assertThat(player.canHit(), is(false));
    }

    @Test
    public void cannotHitIfStand() {
        player.stand();

        assertThat(player.canHit(), is(false));
    }
}