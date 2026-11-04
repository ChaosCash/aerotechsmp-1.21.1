package de.chaos_cash.aerotechsmp.util;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.phases.TimedPhase;
import de.chaos_cash.aerotechsmp.phases.PhaseHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.Objects;
import java.util.UUID;


@EventBusSubscriber(modid = AerotechSMP.MOD_ID)
public class ChatHandler {
    public static int ticksPerHour = 72000;
    public static int ticksPerMinute = 1200;
    public static int ticksPerSecond = 20;


    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();


        var aeroCommand = Commands.literal("AeroTech");

        // Phase command
        aeroCommand.then(Commands.literal("phase")
            .requires((stack) -> stack.hasPermission(Commands.LEVEL_ADMINS))
            .then(Commands.literal("start")
                .then(Commands.literal("PVP")
                    .executes(context1 -> PhaseHandler.schedulePVPPhaseStartCommand())
                )
            )


            .then(Commands.literal("skip")
                .requires((stack) -> PhaseHandler.getPhase() instanceof TimedPhase)
                .then(Commands.argument("Force", BoolArgumentType.bool())
                    .executes((context) -> {

                        if (PhaseHandler.getPhase() instanceof TimedPhase timedPhase) {
                            var shouldForce = BoolArgumentType.getBool(context, "Force");
                            timedPhase.skip(shouldForce);
                            return 1;
                        }
                        else {
                            context.getSource().sendFailure(Component.literal("The current Phase is not Skippable!"));
                            return 0;
                        }
                    })
                )
            )
        );


        dispatcher.register(aeroCommand);
    }


    public static void updateCommands(ServerPlayer player){
        player.server.getCommands().sendCommands(player);
    }


    public static void broadcast(Component message) {
        AerotechSMP.getServer().getPlayerList().broadcastSystemMessage(message, false);
    }


    public static String formatTickTime(int ticks) {return formatTickTime(ticks, false);}


    public static String formatTickTime(int ticks, boolean approximate) {
        int hours = Math.floorDiv(ticks, ticksPerHour);
        ticks -= hours * ticksPerHour;
        int minutes = Math.floorDiv(ticks, ticksPerMinute);
        ticks -= minutes * ticksPerMinute;
        int seconds = Math.floorDiv(ticks, ticksPerSecond);

        String formattedTime = "";

        if (hours > 0){
            formattedTime += hours;
            if (hours == 1)
                formattedTime += " hour";
            else
                formattedTime += " hours";

            if (approximate)
                return formattedTime;
        }

        if (minutes > 0) {
            if (!formattedTime.isEmpty())
                formattedTime += " ";

            formattedTime += minutes;
            if (minutes == 1)
                formattedTime += " minute";
            else
                formattedTime += " minutes";

            if (approximate)
                return formattedTime;
        }

        if (seconds > 0 || (minutes <= 0 && hours <= 0)) {
            if (!formattedTime.isEmpty())
                formattedTime += " ";

            formattedTime += seconds;
            if (seconds == 1)
                formattedTime += " second";
            else
                formattedTime += " seconds";
        }

        return formattedTime;
    }


    public static void sendMessage(Player player, Component message) {sendMessage(player.getUUID(), message);}


    public static void sendMessage(UUID playerId, Component message) {
        sendMessage(Objects.requireNonNull(AerotechSMP.getServer().getPlayerList().getPlayer(playerId)), message);
    }


    public static void sendMessage(ServerPlayer player, Component message) {
        player.sendSystemMessage(
            message
        );
    }
}