package com.known_as_bmf.magic_mirror.block.entity;

import java.util.UUID;

import com.known_as_bmf.magic_mirror.registry.ModBlocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Nameable;

public class AltarBlockEntity extends BlockEntity implements Nameable {
    private static final String CUSTOM_NAME_KEY = "CustomName";
    private static final String ALTAR_UUID_KEY = "AltarUuid";

    private UUID uuid;
    private Text customName;

    public AltarBlockEntity() {
        super(ModBlocks.ALTAR_BLOCK_ENTITY);
    }

    public void initialize() {
        this.uuid = UUID.randomUUID();
    }

    public boolean hasAltarUuid() {
        return this.uuid != null;
    }

    public UUID getAltarUuid() {
        return this.uuid;
    }

    public void setAltarUuid(UUID uuid) {
        this.uuid = uuid;
        this.markDirty();
    }

    public Text getName() {
        return this.hasCustomName() ? this.customName : this.getDefaultName();
    }

    @Override
    public Text getCustomName() {
        return this.customName;
    }

    public Text getDefaultName() {
        return new TranslatableText("magic_mirror.altar");
    }

    public void setCustomName(Text customName) {
        this.customName = customName;
        this.markDirty();
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);

        if (tag.contains(CUSTOM_NAME_KEY)) {
            this.customName = Text.Serializer.fromJson(tag.getString(CUSTOM_NAME_KEY));
        }

        if (tag.containsUuid(ALTAR_UUID_KEY)) {
            this.uuid = tag.getUuid(ALTAR_UUID_KEY);
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);

        if (this.hasCustomName()) {
            tag.putString(CUSTOM_NAME_KEY, Text.Serializer.toJson(this.customName));
        }

        if (this.hasAltarUuid()) {
            tag.putUuid(ALTAR_UUID_KEY, this.uuid);
        }

        return tag;
    }
}
