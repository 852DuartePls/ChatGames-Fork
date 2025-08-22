package me.RareHyperIon.ChatGames.games;

import me.RareHyperIon.ChatGames.ChatGames;
import me.RareHyperIon.ChatGames.handlers.LanguageHandler;
import me.RareHyperIon.ChatGames.utility.Utility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Random;

public class ActiveGame {

    private final LanguageHandler language;
    private final GameConfig config;
    private final ChatGames plugin;

    public final Map.Entry<String, String> question;

    public ActiveGame(final ChatGames plugin, final @NotNull GameConfig config, final LanguageHandler language) {
        this.plugin = plugin;
        this.config = config;
        this.language = language;

        final Random random = new Random();

        this.question = config.choices.get(random.nextInt(config.choices.size()));

        this.start();
    }

    public void start() {
        log("Game \"{}\" has started.", config.name);
        broadcast("GameStart", null);
    }

    public void end() {
        log("Game \"{}\" has ended.", config.name);
        broadcast("GameEnd", null);
    }

    public void win(final @NotNull Player winner) {
        log("Player \"{}\" has won \"{}\"", winner.getName(), config.name);
        broadcast("GameWin", winner);

        Bukkit.getScheduler().runTask(plugin, () -> {
            for (final String command : config.commands) {
                String parsed = command
                        .replace("{player}", winner.getName())
                        .replace("%player%", winner.getName());
                try {
                    if (!Bukkit.dispatchCommand(Bukkit.getConsoleSender(), parsed)) {
                        plugin.getComponentLogger().warn("Reward Command failed to execute: {}", parsed);
                    }
                } catch (Exception e) {
                    plugin.getComponentLogger().error("Error executing command {}: {}", parsed, e.getMessage());
                }
            }
        });
    }

    private void log(String message, Object... args) {
        if (plugin.logFull()) {
            plugin.getComponentLogger().info(message, args);
        }
    }

    private void broadcast(String key, Player contextPlayer) {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(Utility.color(
                    language.get(key)
                            .replace("{prefix}", language.get("Prefix"))
                            .replace("{player}", contextPlayer != null ? contextPlayer.getName() : player.getName())
                            .replace("{name}", config.name)
                            .replace("{timeout}", String.valueOf(config.timeout))
                            .replace("{descriptor}", config.descriptor)
                            .replace("{question}", question.getKey())
                            .replace("{answer}", question.getValue())
                            .replace("\\n", "\n")
            ));
        }
    }
}
