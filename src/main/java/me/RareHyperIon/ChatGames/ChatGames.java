package me.RareHyperIon.ChatGames;

import me.RareHyperIon.ChatGames.commands.ChatGameCommand;
import me.RareHyperIon.ChatGames.handlers.GameHandler;
import me.RareHyperIon.ChatGames.handlers.LanguageHandler;
import me.RareHyperIon.ChatGames.listeners.PlayerListener;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public final class ChatGames extends JavaPlugin {

    private final ComponentLogger LOGGER = ComponentLogger.logger("ChatGamesZ");

    private final LanguageHandler languageHandler;
    private final GameHandler gameHandler;
    private final File chatGamesFolder = new File(Bukkit.getPluginsFolder(), "ChatGames");

    public ChatGames() {
        this.saveDefaultConfig();

        this.languageHandler = new LanguageHandler(this);
        this.gameHandler = new GameHandler(this, this.languageHandler);
    }

    @Override
    public void onLoad() {
        this.languageHandler.load();
    }

    @Override
    public void onEnable() {
        this.gameHandler.load();

        PluginCommand command = this.getCommand("chatgames");
        if (command != null) command.setExecutor(new ChatGameCommand(this));
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this.gameHandler), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    public void reload() {
        LOGGER.info("Reloading...");
        this.reloadConfig();
        this.gameHandler.reload();
        this.languageHandler.load();
    }

    public boolean logFull() {
        return Objects.equals(this.getConfig().getString("LOG_TYPE"), "FULL");
    }

    public @NotNull ComponentLogger getComponentLogger() {
        return LOGGER;
    }

    @Override
    public void saveDefaultConfig() {
        if (!chatGamesFolder.exists() && !chatGamesFolder.mkdirs()) {
            throw new IllegalStateException("Could not create ChatGames folder");
        }

        File configFile = new File(chatGamesFolder, "config.yml");
        if (!configFile.exists()) {
            try {
                Files.copy(Objects.requireNonNull(getClass().getClassLoader()
                        .getResourceAsStream("config.yml")), configFile.toPath());
            } catch (IOException e) {
                throw new RuntimeException("Failed to create config.yml in ChatGames folder", e);
            }
        }
    }

    public @NotNull File getChatGamesFolder() {
        return chatGamesFolder;
    }
}
