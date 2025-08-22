package me.RareHyperIon.ChatGames.handlers;

import me.RareHyperIon.ChatGames.ChatGames;
import me.RareHyperIon.ChatGames.games.ActiveGame;
import me.RareHyperIon.ChatGames.games.GameConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

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
        if(Bukkit.getOnlinePlayers().size() < this.minimumPlayers) return;
        if(this.game != null) return;

        final GameConfig config = this.games.get(this.random.nextInt(this.games.size()));
        this.game = new ActiveGame(this.plugin, config, this.language);

        this.task = Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            this.game.end();
            this.game = null;
            this.task = null;
        }, config.timeout * 20L);
    }

    public final void win(final Player player) {
        this.task.cancel();
        this.task = null;

        this.game.win(player);
        this.game = null;
    }

    public final ActiveGame getGame() {
        return this.game;
    }

    public final void load() {
        try {
            final GameConfig config = new GameConfig(this.plugin);
            this.games.add(config);
        } catch (Exception e) {
            plugin.getComponentLogger().error("[ChatGames] Failed to load games.yml: {}", e.getMessage());
        }

        final int interval = this.plugin.getConfig().getInt("GameInterval");
        this.minimumPlayers = this.plugin.getConfig().getInt("MinimumPlayers");

        this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, this::interval, 0, interval * 20L);

        plugin.getComponentLogger().info("[ChatGames] Loaded games.");
    }

    public final void reload() {
        this.games.clear();
        Bukkit.getScheduler().cancelTask(this.taskId);
        this.load();
    }

}
