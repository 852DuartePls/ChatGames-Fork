package me.RareHyperIon.ChatGames.utility;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class Utility {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacyAmpersand();

    private Utility() { /* prevent instantiation */ }

    @Contract("_ -> new")
    public static @NotNull Component color(final @NotNull String string) {
        if (string.contains("<") && string.contains(">")) {
            // Treat as MiniMessage
            return MINI_MESSAGE.deserialize(string);
        } else if (string.contains("&")) {
            // Treat as legacy color codes
            return LEGACY.deserialize(string);
        } else {
            // Plain text
            return Component.text(string);
        }
    }
}
