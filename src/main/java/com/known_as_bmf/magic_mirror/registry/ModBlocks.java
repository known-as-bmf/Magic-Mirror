package com.known_as_bmf.magic_mirror.registry;

import com.known_as_bmf.magic_mirror.MagicMirrorConstants;
import com.known_as_bmf.magic_mirror.block.AltarBlock;
import com.known_as_bmf.magic_mirror.block.entity.AltarBlockEntity;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {
    public static final AltarBlock ALTAR_BLOCK = new AltarBlock(
            FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(4.0f));

    public static BlockEntityType<AltarBlockEntity> ALTAR_BLOCK_ENTITY;

    private ModBlocks() {
    }

    public static void registerBlocks() {
        Registry.register(Registry.BLOCK,
                new Identifier(MagicMirrorConstants.MOD_ID, MagicMirrorConstants.ALTAR_BLOCK_ID), ALTAR_BLOCK);

        registerBlockEntities();
    }

    private static void registerBlockEntities() {
        ALTAR_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(MagicMirrorConstants.MOD_ID, MagicMirrorConstants.ALTAR_BLOCK_ID),
                BlockEntityType.Builder.create(AltarBlockEntity::new, ALTAR_BLOCK).build(null));
    }
}
