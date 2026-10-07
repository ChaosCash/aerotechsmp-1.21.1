package de.chaos_cash.aerotechsmp.chat;

import com.mojang.brigadier.CommandDispatcher;
import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.phases.PhaseHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;



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