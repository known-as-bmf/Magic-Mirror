package com.known_as_bmf.magic_mirror.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.known_as_bmf.magic_mirror.MagicMirror;
import com.known_as_bmf.magic_mirror.MagicMirrorConstants;
import com.known_as_bmf.magic_mirror.config.model.Config;

import net.fabricmc.loader.api.FabricLoader;

public class ConfigurationProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File FILE = new File(
            FabricLoader.getInstance().getConfigDir() + MagicMirrorConstants.COMMON_CONFIG_NAME);

    private static Config configInstance = null;

    private ConfigurationProvider() {
    }

    public static void loadConfiguration() {
        getConfig();
    }

    public static Config getConfig() {
        return getConfig(false);
    }

    public static Config getConfig(boolean forceReload) {
        if (configInstance == null || forceReload) {
            configInstance = setup();
        }

        return configInstance;
    }

    private static Config setup() {
        if (!FILE.exists()) {
            MagicMirror.LOGGER.debug("No config file found");

            return ConfigurationProvider.createDefaultConfigFile();
        }

        try {
            MagicMirror.LOGGER.debug("Reading config file {}", FILE.getName());

            FileReader fileReader = new FileReader(FILE);
            Config config = GSON.fromJson(fileReader, Config.class);
            return config != null ? config : new Config();
        } catch (IOException | JsonIOException | JsonSyntaxException e) {
            MagicMirror.LOGGER.error(e.getMessage(), e);

            return new Config();
        }
    }

    private static Config createDefaultConfigFile() {
        Config config = new Config();

        try (FileWriter fileWriter = new FileWriter(FILE)) {
            MagicMirror.LOGGER.debug("Creating config file {}", FILE.getName());
            fileWriter.write(GSON.toJson(config));
            MagicMirror.LOGGER.debug("Config file created");
        } catch (IOException e) {
            MagicMirror.LOGGER.error(e.getMessage(), e);
        }

        return config;
    }
}
