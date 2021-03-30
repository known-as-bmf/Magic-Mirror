package com.known_as_bmf.magic_mirror.state;

import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class Altar {
    public static final String ALTAR_UUID = "AltarUuid";

    public static final String ALTAR_POSITION = "AltarPosition";
    public static final String ALTAR_POSITION_X = "X";
    public static final String ALTAR_POSITION_Y = "Y";
    public static final String ALTAR_POSITION_Z = "Z";

    public static final String ALTAR_DIMENSION = "AltarDimension";
    public static final String ALTAR_DIMENSION_NAMESPACE = "Namespace";
    public static final String ALTAR_DIMENSION_PATH = "Path";

    private UUID altarUuid;
    private BlockPos altarBlockPos;
    private Identifier altarDimensionId;

    private Altar() {
    }

    public UUID getUuid() {
        return this.altarUuid;
    }

    public void setUuid(UUID uuid) {
        this.altarUuid = uuid;
    }

    public BlockPos getBlockPos() {
        return this.altarBlockPos.toImmutable();
    }

    public void setBlockPos(BlockPos pos) {
        this.altarBlockPos = pos;
    }

    public Identifier getDimensionIdentifier() {
        return this.altarDimensionId;
    }

    public void setDimensionIdentifier(Identifier id) {
        this.altarDimensionId = id;
    }

    public static Altar of(UUID altarUuid, BlockPos altarBlockPos, Identifier altarDimensionId) {
        Altar altar = new Altar();

        altar.setUuid(altarUuid);
        altar.setBlockPos(altarBlockPos);
        altar.setDimensionIdentifier(altarDimensionId);

        return altar;
    }

    public static Altar of(CompoundTag tag) {
        Altar altar = new Altar();

        // load the tag
        altar.fromTag(tag);

        return altar;
    }

    public void fromTag(CompoundTag tag) {
        if (tag.containsUuid(ALTAR_UUID)) {
            this.altarUuid = tag.getUuid(ALTAR_UUID);
        }

        if (tag.contains(ALTAR_POSITION, 10)) { // 10 is sub tag
            CompoundTag positionTag = tag.getCompound(ALTAR_POSITION);
            if (positionTag.contains(ALTAR_POSITION_X) && positionTag.contains(ALTAR_POSITION_Y)
                    && positionTag.contains(ALTAR_POSITION_Z)) {
                this.altarBlockPos = new BlockPos(positionTag.getInt(ALTAR_POSITION_X),
                        positionTag.getInt(ALTAR_POSITION_Y), positionTag.getInt(ALTAR_POSITION_Z));
            }
        }

        if (tag.contains(ALTAR_DIMENSION, 10)) { // 10 is sub tag
            CompoundTag dimensionTag = tag.getCompound(ALTAR_DIMENSION);
            if (dimensionTag.contains(ALTAR_DIMENSION_NAMESPACE) && dimensionTag.contains(ALTAR_DIMENSION_PATH)) {
                this.altarDimensionId = new Identifier(dimensionTag.getString(ALTAR_DIMENSION_NAMESPACE),
                        dimensionTag.getString(ALTAR_DIMENSION_PATH));
            }
        }
    }

    public CompoundTag toTag(CompoundTag tag) {
        if (this.altarUuid != null) {
            tag.putUuid(ALTAR_UUID, this.altarUuid);
        }

        if (this.altarBlockPos != null) {
            CompoundTag positionTag = new CompoundTag();

            positionTag.putInt(ALTAR_POSITION_X, this.altarBlockPos.getX());
            positionTag.putInt(ALTAR_POSITION_Y, this.altarBlockPos.getY());
            positionTag.putInt(ALTAR_POSITION_Z, this.altarBlockPos.getZ());

            tag.put(ALTAR_POSITION, positionTag);
        }

        if (this.altarDimensionId != null) {
            CompoundTag dimensionTag = new CompoundTag();

            dimensionTag.putString(ALTAR_DIMENSION_NAMESPACE, this.altarDimensionId.getNamespace());
            dimensionTag.putString(ALTAR_DIMENSION_PATH, this.altarDimensionId.getPath());

            tag.put(ALTAR_DIMENSION, dimensionTag);
        }

        return tag;
    }
}
