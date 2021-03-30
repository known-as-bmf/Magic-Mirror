package com.known_as_bmf.magic_mirror.block;

import java.util.Optional;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.ImmutableList.Builder;
import com.known_as_bmf.magic_mirror.block.entity.AltarBlockEntity;
import com.known_as_bmf.magic_mirror.item.MagicMirrorItem;
import com.known_as_bmf.magic_mirror.server.world.IAltarServerWorld;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Dismounting;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockView;
import net.minecraft.world.CollisionView;
import net.minecraft.world.World;

public class AltarBlock extends Block implements BlockEntityProvider {
    private static final ImmutableList<Vec3i> HORIZONTAL_SPAWN_SLICE;
    private static final ImmutableList<Vec3i> SPAWN_BLOCKS;

    public AltarBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (blockEntity instanceof AltarBlockEntity) {
            AltarBlockEntity altarBlockEntity = (AltarBlockEntity) blockEntity;

            if (itemStack.hasCustomName()) {
                altarBlockEntity.setCustomName(itemStack.getName());
            }

            if (!world.isClient()) {
                if (!altarBlockEntity.hasAltarUuid()) {
                    altarBlockEntity.initialize();
                }

                IAltarServerWorld serverWorld = (IAltarServerWorld) world;
                serverWorld.placeAltar(altarBlockEntity);
            }
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);

        if (stack.getItem() instanceof MagicMirrorItem) {
            if (!world.isClient()) {
                AltarBlockEntity entity = (AltarBlockEntity) world.getBlockEntity(pos);

                MagicMirrorItem.setTargetAltar(stack, entity.getAltarUuid());

                return ActionResult.SUCCESS;
            } else {
                return ActionResult.CONSUME;
            }
        }

        return ActionResult.PASS;
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (!world.isClient() && blockEntity instanceof AltarBlockEntity) {
            AltarBlockEntity altarBlockEntity = (AltarBlockEntity) blockEntity;
            IAltarServerWorld serverWorld = (IAltarServerWorld) world;

            serverWorld.breakAltar(altarBlockEntity);
        }

        super.onBreak(world, pos, state, player);
    }

    @Override
    public PistonBehavior getPistonBehavior(BlockState state) {
        return PistonBehavior.BLOCK;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new AltarBlockEntity();
    }

    public static Optional<Vec3d> findRespawnPosition(EntityType<?> entity, CollisionView collisionView, BlockPos pos) {
        Optional<Vec3d> optional = method_30842(entity, collisionView, pos, true);
        return optional.isPresent() ? optional : method_30842(entity, collisionView, pos, false);
    }

    private static Optional<Vec3d> method_30842(EntityType<?> entityType, CollisionView collisionView,
            BlockPos blockPos, boolean bl) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        UnmodifiableIterator<Vec3i> var5 = SPAWN_BLOCKS.iterator();

        Vec3d vec3d;
        do {
            if (!var5.hasNext()) {
                return Optional.empty();
            }

            Vec3i vec3i = var5.next();
            mutable.set(blockPos).move(vec3i);
            vec3d = Dismounting.method_30769(entityType, collisionView, mutable, bl);
        } while (vec3d == null);

        return Optional.of(vec3d);
    }

    static {
        HORIZONTAL_SPAWN_SLICE = ImmutableList.of(
                //
                new Vec3i(0, 0, -1),
                //
                new Vec3i(-1, 0, 0),
                //
                new Vec3i(0, 0, 1),
                //
                new Vec3i(1, 0, 0),
                //
                new Vec3i(-1, 0, -1),
                //
                new Vec3i(1, 0, -1),
                //
                new Vec3i(-1, 0, 1),
                //
                new Vec3i(1, 0, 1));

        SPAWN_BLOCKS = (new Builder<Vec3i>())
                //
                .addAll(HORIZONTAL_SPAWN_SLICE)
                //
                .addAll(HORIZONTAL_SPAWN_SLICE.stream().map(Vec3i::down).iterator())
                //
                .addAll(HORIZONTAL_SPAWN_SLICE.stream().map(Vec3i::up).iterator())
                //
                .add(new Vec3i(0, 1, 0))
                //
                .build();
    }
}
