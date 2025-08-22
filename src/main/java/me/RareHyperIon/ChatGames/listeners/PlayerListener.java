package me.RareHyperIon.ChatGames.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.RareHyperIon.ChatGames.games.ActiveGame;
import me.RareHyperIon.ChatGames.handlers.GameHandler;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class PlayerListener implements Listener {

    private final GameHandler handler;

    public PlayerListener(final GameHandler handler) {
        this.handler = handler;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerChat(final AsyncChatEvent event) {
        final ActiveGame game = this.handler.getGame();
        if (game == null) return;

        String messageText = PlainTextComponentSerializer.plainText().serialize(event.message());
        String answer = game.question.getValue();

        // Normalize text: lowercase, remove punctuation, trim extra spaces
        messageText = normalize(messageText);
        answer = normalize(answer);

        if (messageText.contains(answer)) {
            this.handler.win(event.getPlayer());
        }
    }

    private @NotNull String normalize(@NotNull String text) {
        return text.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "") // remove punctuation
                .replaceAll("\\s+", " ")        // collapse multiple spaces
                .trim();                                        // remove leading/trailing spaces
    }
}
