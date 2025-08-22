package me.RareHyperIon.ChatGames.handlers;

import me.RareHyperIon.ChatGames.ChatGames;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class LanguageHandler {

    private final ChatGames plugin;
    private final Map<String, String> translations = new HashMap<>();

    public LanguageHandler(final ChatGames plugin) {
        this.plugin = plugin;
    }

    public final void load() {
        final String language = this.plugin.getConfig().getString("Language", "EN-US");

        // Use ChatGames folder from main plugin class
        File languageFolder = new File(plugin.getChatGamesFolder(), "language");
        if (!languageFolder.exists() && !languageFolder.mkdirs()) {
            throw new IllegalStateException("Failed to create ChatGames/language folder.");
        }

        File file = new File(languageFolder, language + ".yml");
        if (!file.exists()) {
            this.saveDefault(file);
        }

        FileConfiguration langConfig = YamlConfiguration.loadConfiguration(file);

        translations.clear();
        for (String key : langConfig.getKeys(false)) {
            translations.put(key, langConfig.getString(key));
        }

        plugin.getComponentLogger().info("Loaded language: {}", language);
    }

    public final String get(final String key) {
        return translations.get(key);
    }

    private void saveDefault(File targetFile) {
        try (InputStream stream = plugin.getResource("language/" + targetFile.getName())) {
            if (stream == null) throw new IllegalStateException("Resource not found in jar: " + targetFile.getName());
            Files.copy(stream, targetFile.toPath());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create default language file: " + targetFile.getName(), e);
        }
        plugin.getComponentLogger().info("Created default language file: {}", targetFile.getName());
    }
}
