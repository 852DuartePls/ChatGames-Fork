package me.RareHyperIon.ChatGames.handlers;

import me.RareHyperIon.ChatGames.ChatGames;
import me.RareHyperIon.ChatGames.games.ActiveGame;
import me.RareHyperIon.ChatGames.games.GameConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameHandler {

    private final ChatGames plugin;
    private final LanguageHandler language;

    private final List<GameConfig> games = new ArrayList<>();
    private final Random random = new Random();

    private int minimumPlayers, taskId;
    private ActiveGame game;
    private BukkitTask task;

    public GameHandler(final ChatGames plugin, final LanguageHandler language) {
        this.plugin = plugin;
        this.language = language;
    }

    public final void interval() {
        if (Bukkit.getOnlinePlayers().size() < minimumPlayers) return;
        if (game != null) return;

        final GameConfig config = games.get(random.nextInt(games.size()));
        game = new ActiveGame(plugin, config, language);

        task = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            game.end();
            game = null;
            task = null;
        }, config.timeout * 20L);
    }

    public final void win(final Player player) {
        if (task != null) task.cancel();
        task = null;

        if (game != null) game.win(player);
        game = null;
    }

    public final ActiveGame getGame() {
        return game;
    }

    public final void load() {
        final File folder = new File(plugin.getChatGamesFolder(), "games");

        if (!folder.exists()) saveDefault(folder);

        final File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".yml"));
        if (files == null || files.length == 0) {
            plugin.getComponentLogger().warn("No games found in games folder!");
            return;
        }

        for (final File file : files) {
            try {
                games.add(new GameConfig(YamlConfiguration.loadConfiguration(file)));
            } catch (Exception e) {
                plugin.getComponentLogger().error("Failed to load {}: {}", file.getName(), e.getMessage());
            }
        }

        minimumPlayers = plugin.getConfig().getInt("MinimumPlayers");
        int interval = plugin.getConfig().getInt("GameInterval");
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::interval, 0, interval * 20L);

        plugin.getComponentLogger().info("Loaded {} games.", games.size());
    }

    private void saveDefault(@NotNull File folder) {
        if (!folder.mkdirs() && !folder.exists())
            throw new IllegalStateException("Failed to create games folder.");

        for (final String game : List.of("trivia.yml", "math.yml", "unscramble.yml")) {
            final File out = new File(folder, game);
            try (InputStream stream = plugin.getResource("games/" + game)) {
                if (stream == null) continue;
                Files.copy(stream, out.toPath());
            } catch (IOException e) {
                plugin.getComponentLogger().error("Failed to copy default game {}: {}", game, e.getMessage());
            }
        }

        plugin.getComponentLogger().info("Created default game configurations.");
    }

    public final void reload() {
        games.clear();
        Bukkit.getScheduler().cancelTask(taskId);
        load();
    }
}
