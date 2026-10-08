package de.chaos_cash.aerotechsmp.chat;

import com.mojang.brigadier.CommandDispatcher;
import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.EventHandler;
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

import java.util.ArrayList;


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



    public static void broadcast(Component message)
    {
        AerotechSMP.getServer().getPlayerList().broadcastSystemMessage(message, false);
    }



    public static void broadcastCountdown(int ticks, String prefix, String suffix, Component countDownOverMessage)
    {
        int hours = Math.floorDiv(ticks, 720000);
        ticks -= hours * 720000;
        int minutes = Math.floorDiv(ticks, 1200);
        ticks -= minutes * 1200;
        int seconds = Math.floorDiv(ticks, 20);

        var messages = new ArrayList<Component>();

        for (int hour = 1; hour <= hours; hour++)
        {

        }
    }


    static Component createCountdownMessage(String prefix, String suffix, int seconds, int minutes, int hours)
    {
        String message = prefix;
        if (minutes > 0)
            message += minutes + "m ";
        if (hours > 0)
            message += minutes + "m ";
        message += seconds + "s " + suffix;

        return Component.literal(message);
    }



    static int timeToTick(int seconds, int minutes, int hours)
    {
        return seconds * 20 + minutes * 1200 + hours * 720000;
    }



    public static void broadcast(Component message, int tickDelay)
    {
        EventHandler.scheduleTask(tickDelay,() -> {broadcast(message);});
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