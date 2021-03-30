package com.known_as_bmf.magic_mirror.config.model;

public class Config {
    /**
     * In seconds.
     */
    private int cooldown;
    private int uses;
    public final Cost cost;
    public final Debuff debuff;

    public Config() {
        this.cooldown = 5;
        this.cost = new Cost();
        this.debuff = new Debuff();
    }

    public Config(Config config) {
        this.cooldown = config.cooldown;
        this.cost = config.cost;
        this.debuff = config.debuff;
    }

    public int getCooldownDuration() {
        return this.cooldown * 20; // convert seconds to minecraft ticks
    }

    public int getUses() {
        return this.uses;
    }
}
