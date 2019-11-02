package com.github.ikhoury.twentyone.player;

import com.github.ikhoury.twentyone.deck.Card;
import org.junit.Before;
import org.junit.Test;

import static com.github.ikhoury.twentyone.Constants.BANK_STAND_THRESHOLD;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class BankTest {

    private Bank bank;

    @Before
    public void setUp() {
        bank = new Bank();
    }

    @Test
    public void canHitIfUnderThreshold() {
        bank.hit(Card.ACE, BANK_STAND_THRESHOLD - 1);
        assertThat(bank.canHit(), is(true));
    }

    @Test
    public void canHitIfAtThreshold() {
        bank.hit(Card.ACE, BANK_STAND_THRESHOLD);
        assertThat(bank.canHit(), is(true));
    }

    @Test
    public void standsIfOverThreshold() {
        bank.hit(Card.ACE, BANK_STAND_THRESHOLD + 1);
        assertThat(bank.isStanding(), is(true));
    }
}