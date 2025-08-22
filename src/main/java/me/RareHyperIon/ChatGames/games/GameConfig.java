package me.RareHyperIon.ChatGames.games;

import me.RareHyperIon.ChatGames.ChatGames;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameConfig {

    public final String name, descriptor;

    public final List<Map.Entry<String, String>> choices;
    public final List<String> commands;

    public final int timeout;

    public GameConfig(final @NotNull ChatGames plugin) {

        File file = getFile();

        if (!file.exists()) {
            plugin.saveResource("games.yml", false); // false = don’t overwrite
        }

        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        this.name = configuration.getString("name");
        this.descriptor = configuration.getString("descriptor");
        this.commands = configuration.getStringList("reward-commands");
        this.timeout = configuration.getInt("timeout");
        this.choices = this.parse(configuration.getList("questions"));
    }

    private static @NotNull File getFile() {
        File dataFolder = new File(Bukkit.getPluginsFolder(), "ChatGames");
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            throw new RuntimeException("Could not create ChatGames folder inside plugins.");
        }

        File file = new File(dataFolder, "games.yml");
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new IOException("File games.yml already exists but could not be created.");
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not create games.yml inside ChatGames folder", e);
            }
        }
        return file;
    }

    @Contract("null -> fail")
    private @NotNull List<Map.Entry<String, String>> parse(final List<?> list) {
        final List<Map.Entry<String, String>> choices = new ArrayList<>();

        if (list == null) throw new IllegalArgumentException("Game \"" + this.name + "\" does not contain questions.");

        for (final Object object : list) {
            if (!(object instanceof List<?> choice)) continue;
            if (choice.size() < 2) continue;

            choices.add(new AbstractMap.SimpleEntry<>((String) choice.get(0), (String) choice.get(1)));
        }

        return choices;
    }

}
