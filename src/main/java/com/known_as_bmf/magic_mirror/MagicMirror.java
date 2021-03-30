package com.known_as_bmf.magic_mirror;

import com.known_as_bmf.magic_mirror.config.ConfigurationProvider;
import com.known_as_bmf.magic_mirror.registry.ModBlocks;
import com.known_as_bmf.magic_mirror.registry.ModItems;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;

public class MagicMirror implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger(MagicMirrorConstants.MOD_NAME);

    @Override
    public void onInitialize() {
        LOGGER.info("Loading up {}!", MagicMirrorConstants.MOD_NAME);

        ConfigurationProvider.loadConfiguration();

        ModItems.registerItems();
        ModBlocks.registerBlocks();
    }
}
