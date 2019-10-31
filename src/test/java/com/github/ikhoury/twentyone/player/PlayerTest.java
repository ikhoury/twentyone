package com.github.ikhoury.twentyone.player;

import com.github.ikhoury.twentyone.deck.Card;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class PlayerTest {

    private static final String PLAYER_NAME = "John Doe";
    private static final int INITIAL_PLAYER_POINTS = 0;

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
        int points = 1;

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
    public void busts() {
        player.bust();

        assertThat(player.isBust(), is(true));
    }
}