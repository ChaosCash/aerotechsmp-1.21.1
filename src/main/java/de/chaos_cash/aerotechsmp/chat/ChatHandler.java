package de.chaos_cash.aerotechsmp.chat;

import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.phases.PhaseHandler;
import dev.architectury.neoforge.ArchitecturyNeoForge;
import dev.architectury.utils.ArchitecturyConstants;
import dev.ftb.mods.ftblibrary.FTBLibrary;
import dev.ftb.mods.ftblibrary.neoforge.FTBLibraryNeoForge;
import net.creeperhost.ftbbackups.BackupHandler;
import net.creeperhost.ftbbackups.FTBBackups;
import net.creeperhost.ftbbackups.data.Backup;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeConfig;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.ServerChatEvent;

import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;


@EventBusSubscriber(modid = AerotechSMP.MODID)
public class ChatHandler {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(
            Commands.literal("phase")
                .then(Commands.literal("start")
                    .then(Commands.literal("PVP")
                        .executes(PhaseHandler::schedulePVPPhaseStartCommand)
                    )
                )
        );
    }



    public static void Broadcast(Component message)
    {
        AerotechSMP.getServer().getPlayerList().broadcastSystemMessage(message, false);
    }



    public static void sendMessage(Player player, String message)
    {
        PlayerChatMessage chatMessage = PlayerChatMessage.unsigned(player.getUUID(), message);

        player.createCommandSourceStack().sendChatMessage(
            new OutgoingChatMessage.Player(chatMessage),
            false,
            ChatType.bind(ChatType.MSG_COMMAND_INCOMING, player)
        );
    }
}