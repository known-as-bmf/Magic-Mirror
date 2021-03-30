package com.known_as_bmf.magic_mirror.config.model;

public class Debuff {
    private int hunger;
    private int weakness;
    private int fatigue;
    private int blindness;
    private int nausea;

    public Debuff() {
        this.hunger = 0;
        this.weakness = 60;
        this.fatigue = 5;
        this.blindness = 5;
        this.nausea = 0;
    }

    public Debuff(Debuff debuffs) {
        this.hunger = debuffs.hunger;
        this.weakness = debuffs.weakness;
        this.fatigue = debuffs.fatigue;
        this.blindness = debuffs.blindness;
        this.nausea = debuffs.nausea;
    }

    public int getHungerDuration() {
        return this.hunger * 20; // convert seconds to minecraft ticks
    }

    public int getWeaknessDuration() {
        return this.weakness * 20; // convert seconds to minecraft ticks
    }

    public int getFatigueDuration() {
        return this.fatigue * 20; // convert seconds to minecraft ticks
    }

    public int getBlindnessDuration() {
        return this.blindness * 20; // convert seconds to minecraft ticks
    }

    public int getNauseaDuration() {
        return this.nausea * 20; // convert seconds to minecraft ticks
    }
}
