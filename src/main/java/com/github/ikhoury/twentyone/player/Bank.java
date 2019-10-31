package com.github.ikhoury.twentyone.player;

import static com.github.ikhoury.twentyone.Constants.BANK_STAND_THRESHOLD;

public class Bank extends Player {

    private static final String BANK_NAME = "The Bank";

    public Bank() {
        super(BANK_NAME);
    }

    @Override
    public boolean canHit() {
        return super.canHit() && getPoints() < BANK_STAND_THRESHOLD;
    }

    @Override
    public boolean isStand() {
        return super.isStand() || getPoints() >= BANK_STAND_THRESHOLD;
    }
}
