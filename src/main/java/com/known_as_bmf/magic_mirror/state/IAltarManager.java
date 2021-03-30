package com.known_as_bmf.magic_mirror.state;

import java.util.UUID;

public interface IAltarManager {
    Altar getAltar(UUID uuid);

    void addAltar(Altar altar);

    void removeAltar(UUID uuid);
}
