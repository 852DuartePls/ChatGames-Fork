package me.RareHyperIon.ChatGames.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.RareHyperIon.ChatGames.ChatGames;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public class ChatGameCommand implements BasicCommand {

    private final ChatGames plugin;

    public ChatGameCommand(final ChatGames plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        final var sender = source.getSender();

        if (args.length == 0 || !args[0].equalsIgnoreCase("reload")) {
            sender.sendRichMessage("<red>Usage: /cg reload");
            return;
        }

        this.plugin.reload();
        sender.sendRichMessage("<green>Successfully reloaded.");
    }

    @Override
    public @Nullable String permission() {
        return "chatgames.reload";
    }

    @Override
    public Collection<String> suggest(CommandSourceStack source, String[] args) {
        if (args.length == 0) return List.of("reload");
        return List.of();
    }
}
