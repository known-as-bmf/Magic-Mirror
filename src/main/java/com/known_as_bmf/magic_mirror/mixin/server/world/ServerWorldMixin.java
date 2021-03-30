package com.known_as_bmf.magic_mirror.mixin.server.world;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.known_as_bmf.magic_mirror.block.AltarBlock;
import com.known_as_bmf.magic_mirror.block.entity.AltarBlockEntity;
import com.known_as_bmf.magic_mirror.server.world.IAltarServerWorld;
import com.known_as_bmf.magic_mirror.state.Altar;
import com.known_as_bmf.magic_mirror.state.AltarManager;
import com.known_as_bmf.magic_mirror.state.IAltarManager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.entity.EntityType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World implements IAltarServerWorld {
    private IAltarManager altarManager;

    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef,
            DimensionType dimensionType, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed) {
        super(properties, registryRef, dimensionType, profiler, isClient, debugWorld, seed);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConstructed(CallbackInfo ci) {
        ServerWorld overworld;

        if (this.getRegistryKey() == World.OVERWORLD) {
            overworld = (ServerWorld) ((Object) this);
        } else {
            overworld = this.getServer().getOverworld();
        }

        this.altarManager = overworld.getPersistentStateManager().getOrCreate(AltarManager::new,
                AltarManager.getStateKey());
    }

    @Override
    public void placeAltar(AltarBlockEntity entity) {
        Identifier dimensionId = this.getRegistryManager().get(Registry.DIMENSION_TYPE_KEY).getId(this.getDimension());

        Altar altar = Altar.of(entity.getAltarUuid(), entity.getPos(), dimensionId);
        this.altarManager.addAltar(altar);
    }

    @Override
    public void breakAltar(AltarBlockEntity entity) {
        this.altarManager.removeAltar(entity.getAltarUuid());

    }

    @Override
    public boolean teleportToAltar(ServerPlayerEntity player, UUID altarUuid,
            BiConsumer<ServerWorld, BlockPos> onSuccess, Consumer<String> onFailure) {
        Altar altar = this.altarManager.getAltar(altarUuid);

        if (altar != null) {
            ServerWorld targetWorld = this.getServer()
                    .getWorld(RegistryKey.of(Registry.DIMENSION, altar.getDimensionIdentifier()));

            if (targetWorld != null) {
                Optional<Vec3d> maybeSpawnVec = AltarBlock.findRespawnPosition(EntityType.PLAYER, targetWorld,
                        altar.getBlockPos());

                if (maybeSpawnVec.isPresent()) {
                    Vec3d spawnVec = maybeSpawnVec.get();

                    player.teleport(targetWorld, spawnVec.getX(), spawnVec.getY(), spawnVec.getZ(), player.yaw,
                            player.pitch);

                    onSuccess.accept(targetWorld, new BlockPos(spawnVec));

                    return true;
                }
            }
        }

        onFailure.accept("");

        return false;
    }
}
