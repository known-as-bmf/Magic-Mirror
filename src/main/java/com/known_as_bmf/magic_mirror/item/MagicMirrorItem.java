package com.known_as_bmf.magic_mirror.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

import com.known_as_bmf.magic_mirror.MagicMirrorConstants;
import com.known_as_bmf.magic_mirror.config.ConfigurationProvider;
import com.known_as_bmf.magic_mirror.config.model.Config;
import com.known_as_bmf.magic_mirror.server.world.IAltarServerWorld;

public class MagicMirrorItem extends Item {
    /**
     * Compound tag key.
     */
    private static final String TARGET_ALTAR_UUID = "TargetAltarUuid";

    private static final Style TOOLTIP_STYLE;

    public MagicMirrorItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(new TranslatableText(MagicMirrorConstants.MAGIC_MIRROR_ITEM_TOOLTIP).fillStyle(TOOLTIP_STYLE));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (!world.isClient) {
            Config config = ConfigurationProvider.getConfig();

            ServerWorld originWorld = (ServerWorld) world;
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;

            int xpLevelCost = config.cost.getXpLevels();
            BlockPos playerCurrentPosition = serverPlayer.getBlockPos();

            if (serverPlayer.experienceLevel >= xpLevelCost || serverPlayer.abilities.creativeMode) {
                boolean success = ((IAltarServerWorld) originWorld).teleportToAltar(serverPlayer, getTargetAltar(stack),
                        (targetWorld, pos) -> {
                            originWorld.playSound(null, playerCurrentPosition, SoundEvents.ENTITY_CHICKEN_EGG,
                                    SoundCategory.PLAYERS, 1f, 1f);
                            targetWorld.playSound(null, pos, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.PLAYERS, 1f,
                                    1f);
                            // remove experience
                            serverPlayer.setExperienceLevel(serverPlayer.experienceLevel - xpLevelCost);

                            // apply debuffs
                            serverPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS,
                                    config.debuff.getBlindnessDuration(), 0));
                            serverPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS,
                                    config.debuff.getWeaknessDuration(), 0));
                            serverPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE,
                                    config.debuff.getFatigueDuration(), 2));
                            serverPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER,
                                    config.debuff.getHungerDuration(), 0));
                            serverPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA,
                                    config.debuff.getNauseaDuration(), 0));

                            // hurt player
                            serverPlayer.damage(DamageSource.magic(serverPlayer, serverPlayer),
                                    config.cost.getHealthDamage());

                            stack.damage(1, serverPlayer, p -> p.sendToolBreakStatus(hand));

                            serverPlayer.getItemCooldownManager().set(this, config.getCooldownDuration());
                        }, reason -> {
                            serverPlayer.sendSystemMessage(
                                    new TranslatableText(MagicMirrorConstants.MESSAGE_FAILURE_NO_SPAWN_FOUND),
                                    Util.NIL_UUID);
                            originWorld.playSound(null, playerCurrentPosition, SoundEvents.ENTITY_SHULKER_BULLET_HURT,
                                    SoundCategory.PLAYERS, 1f, 1f);
                        });

                if (success) {
                    return TypedActionResult.success(stack);
                } else {
                    return TypedActionResult.fail(stack);
                }

            } else {
                serverPlayer.sendSystemMessage(new TranslatableText(MagicMirrorConstants.MESSAGE_FAILURE_NOT_ENOUGH_XP),
                        Util.NIL_UUID);
                originWorld.playSound(null, playerCurrentPosition, SoundEvents.ENTITY_SHULKER_BULLET_HURT,
                        SoundCategory.PLAYERS, 1f, 1f);

                return TypedActionResult.fail(stack);
            }
        }

        return TypedActionResult.consume(stack);
    }

    public static UUID getTargetAltar(ItemStack stack) {
        CompoundTag compoundTag = stack.getTag();

        if (compoundTag != null && compoundTag.containsUuid(TARGET_ALTAR_UUID)) {
            return compoundTag.getUuid(TARGET_ALTAR_UUID);
        } else {
            return null;
        }
    }

    public static void setTargetAltar(ItemStack stack, UUID uuid) {
        CompoundTag compoundTag = stack.getOrCreateTag();
        compoundTag.putUuid(TARGET_ALTAR_UUID, uuid);
    }

    static {
        TOOLTIP_STYLE = Style.EMPTY.withColor(Formatting.GRAY).withItalic(true);
    }
}