package com.known_as_bmf.magic_mirror.server.world;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.known_as_bmf.magic_mirror.block.entity.AltarBlockEntity;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public interface IAltarServerWorld {
    boolean teleportToAltar(ServerPlayerEntity player, UUID altarUuid, BiConsumer<ServerWorld, BlockPos> onSuccess,
            Consumer<String> onFailure);

    void placeAltar(AltarBlockEntity entity);

    void breakAltar(AltarBlockEntity entity);
}
