package com.known_as_bmf.magic_mirror.state;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.PersistentState;

public class AltarManager extends PersistentState implements IAltarManager {
    private static final String ALTARS_KEY = "Altars";

    private final Map<UUID, Altar> altars = Maps.newHashMap();

    public AltarManager() {
        super(getStateKey());
    }

    public Altar getAltar(UUID uuid) {
        return this.altars.get(uuid);
    }

    public void addAltar(Altar altar) {
        this.altars.put(altar.getUuid(), altar);
        this.markDirty();
    }

    public void removeAltar(UUID uuid) {
        this.altars.remove(uuid);
        this.markDirty();
    }

    @Override
    public void fromTag(CompoundTag tag) {
        ListTag listTag = tag.getList(ALTARS_KEY, 10);

        for (int i = 0; i < listTag.size(); ++i) {
            CompoundTag compoundTag = listTag.getCompound(i);

            Altar altar = Altar.of(compoundTag);
            this.altars.put(altar.getUuid(), altar);
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        ListTag listTag = new ListTag();

        for (Altar altar : this.altars.values()) {
            listTag.add(altar.toTag(new CompoundTag()));
        }

        tag.put(ALTARS_KEY, listTag);

        return tag;
    }

    public static String getStateKey() {
        return ALTARS_KEY;
    }
}
