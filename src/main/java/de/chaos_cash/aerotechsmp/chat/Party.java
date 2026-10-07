package de.chaos_cash.aerotechsmp.chat;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import static com.mojang.brigadier.arguments.StringArgumentType.string;

public class Party {
    public static int createCommand(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        String name = context.getArgument("Name", String.class);
        var player = source.getPlayer();

        source.sendSuccess(() -> Component.literal("name: " + name + "\nplayerName: " + player.getName().getString()), false);
        return 1;
    }
}
