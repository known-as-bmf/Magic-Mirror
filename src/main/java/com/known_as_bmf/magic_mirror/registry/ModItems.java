package com.known_as_bmf.magic_mirror.registry;

import com.known_as_bmf.magic_mirror.MagicMirrorConstants;
import com.known_as_bmf.magic_mirror.item.AltarBlockItem;
import com.known_as_bmf.magic_mirror.item.MagicMirrorItem;
import com.known_as_bmf.magic_mirror.config.ConfigurationProvider;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class ModItems {
    public static final MagicMirrorItem MAGIC_MIRROR = new MagicMirrorItem(
            new FabricItemSettings().group(ItemGroup.TRANSPORTATION).maxCount(1)
                    .maxDamage(ConfigurationProvider.getConfig().getUses()).rarity(Rarity.RARE));

    public static final AltarBlockItem ALTAR_BLOCK_ITEM = new AltarBlockItem(ModBlocks.ALTAR_BLOCK,
            new FabricItemSettings().group(ItemGroup.TRANSPORTATION).maxCount(1).rarity(Rarity.RARE));

    private ModItems() {
    }

    public static void registerItems() {
        Registry.register(Registry.ITEM,
                new Identifier(MagicMirrorConstants.MOD_ID, MagicMirrorConstants.MAGIC_MIRROR_ITEM_ID), MAGIC_MIRROR);

        Registry.register(Registry.ITEM,
                new Identifier(MagicMirrorConstants.MOD_ID, MagicMirrorConstants.ALTAR_BLOCK_ID), ALTAR_BLOCK_ITEM);
    }
}
