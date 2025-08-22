package me.RareHyperIon.ChatGames.utility;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class Utility {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacyAmpersand();

    private Utility() {
        // prevent instantiation
    }

    /**
     * Parses a string using MiniMessage if it detects tags, otherwise falls back to legacy '&' colors.
     */
    @Contract("_ -> new")
    public static @NotNull String color(final @NotNull String string) {
        if (string.contains("<") && string.contains(">")) {
            Component component = MINI_MESSAGE.deserialize(string);
            return LEGACY.serialize(component);
        } else {
            return LEGACY.serialize(LEGACY.deserialize(string));
        }
    }
}
