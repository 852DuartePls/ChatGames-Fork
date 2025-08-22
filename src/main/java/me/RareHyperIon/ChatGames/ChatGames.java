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

import java.util.Objects;

public final class ChatGames extends JavaPlugin {

    private final ComponentLogger LOGGER = ComponentLogger.logger("ChatGamesZ");

    private final LanguageHandler languageHandler;
    private final GameHandler gameHandler;

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
        LOGGER.info("[ChatGames] Reloading...");
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
}
