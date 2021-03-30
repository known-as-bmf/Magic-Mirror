package com.known_as_bmf.magic_mirror.config.model;

public class Cost {
    /**
     * In levels.
     */
    private int xp;
    /**
     * In half-hearts.
     */
    private int health;

    public Cost() {
        this.xp = 2;
        this.health = 0;
    }

    public Cost(Cost cost) {
        this.xp = cost.xp;
        this.health = cost.health;
    }

    public int getXpLevels() {
        return this.xp;
    }

    public int getHealthDamage() {
        return this.health;
    }
}
