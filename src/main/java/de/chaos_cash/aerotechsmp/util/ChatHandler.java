package de.chaos_cash.aerotechsmp.util;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.phases.TimedPhase;
import de.chaos_cash.aerotechsmp.team.RandomItemsReward;
import de.chaos_cash.aerotechsmp.team.Reward;
import de.chaos_cash.aerotechsmp.team.Team;
import de.chaos_cash.aerotechsmp.phases.PhaseHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.server.command.EnumArgument;

import java.awt.*;
import java.util.Objects;
import java.util.UUID;


@EventBusSubscriber(modid = AerotechSMP.MOD_ID)
public class ChatHandler {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();


        var aeroCommand = Commands.literal("AeroTech");

        // Phase command
        aeroCommand.then(Commands.literal("phase")
            .requires((stack) -> stack.hasPermission(Commands.LEVEL_ADMINS))
            .then(Commands.literal("start")
                .then(Commands.literal("PVP")
                    .executes(PhaseHandler::schedulePVPPhaseStartCommand)
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




        // team command
        var teamCommand = Commands.literal("team")
                .requires((source) -> PhaseHandler.getPhase().allowsTeamModifications());

        // /team create
        teamCommand.then(Commands.literal("create")
            .then(Commands.argument("Name", StringArgumentType.string())
                .executes((context) -> {
                    if (Team.get(Objects.requireNonNull(context.getSource().getPlayer())).isPresent()) {
                        context.getSource().sendFailure(Component.literal("You are already part of a team!"));
                        return 0;
                    }

                    Team.create(context.getSource().getPlayer(), StringArgumentType.getString(context,"Name"));
                    return 1;
                })
            )
        );


        // /team invite
        teamCommand.then(Commands.literal("invite")
            .requires(stack -> Team.get(Objects.requireNonNull(stack.getPlayer())).isPresent())
            .then(Commands.argument("Player", EntityArgument.player())
                .executes((context) ->{
                    var source = context.getSource();
                    var team = Team.get(Objects.requireNonNull(source.getPlayer()));

                    if (team.isEmpty()) {source.sendFailure(Component.literal("You are not part of a team!")); return 0;}
                    if (team.get().isFull()) {source.sendFailure(Component.literal("Your team is already full!"));  return 0;}

                    var invitedPlayer = EntityArgument.getPlayer(context, "Player");
                    if (invitedPlayer.getUUID() == source.getPlayer().getUUID()){
                        source.sendFailure(Component.literal("You cant invite yourself!"));
                        return 0;
                    }


                    var teamOfInvitedPlayer = Team.get(invitedPlayer);
                    if (teamOfInvitedPlayer.isPresent())
                        if (teamOfInvitedPlayer == team) {
                            source.sendFailure(Component.literal("This player is already in your team!"));
                            return 0;
                        }

                    team.get().sendInvite(invitedPlayer);

                    source.sendSuccess(() -> Component.literal("Successfully sent an Invite"), false);

                    return 1;
                })
            )
        );


        // /team leave
        teamCommand.then(Commands.literal("leave")
            .requires(stack -> Team.get(Objects.requireNonNull(stack.getPlayer())).isPresent())
            .executes((context) -> {
                var source = context.getSource();
                var player = source.getPlayer();
                assert player != null;
                var team = Team.get(player);

                if (team.isEmpty()) {
                    source.sendFailure(Component.literal("You are not part of a team"));
                    return 0;
                }
                if (team.get().isOwner(player)) {
                    source.sendFailure(Component.literal("You cant leave the team because you are its Owner"));
                    return 0;
                }

                team.get().leave(player);
                source.sendSuccess(() -> Component.literal("Successfully left the team"), false);

                player.server.getCommands().sendCommands(player);

                return 1;
            })
        );


        // /team delete
        teamCommand.then(Commands.literal("delete")
            .requires(stack -> Team.get(Objects.requireNonNull(stack.getPlayer())).isPresent())
            .executes((context) -> {
                var source = context.getSource();
                var player = source.getPlayer();
                assert player != null;
                var team = Team.get(player);

                if (team.isEmpty()) {
                    source.sendFailure(Component.literal("You are not part of a team"));
                    return 0;
                }
                if (!team.get().isOwner(player)) {
                    source.sendFailure(Component.literal("Only the Owner can delete a team"));
                    return 0;
                }

                source.sendSuccess(() -> Component.literal("Successfully deleted the team '" + team.get().getDisplayName() + "'"), false);
                team.get().delete(source);

                return 1;
            })
        );


        // /team acceptInvite
        teamCommand.then(Commands.literal("acceptInvite")
            .executes((context) -> {
                var source = context.getSource();
                var player = source.getPlayer();
                assert player != null;
                var acceptResponse = Team.acceptInvite(player);

                switch (acceptResponse){
                    case Success -> {
                        var team = Team.get(player).get();
                        source.sendSuccess(() -> Component.literal("Successfully joined the team '" + team.getDisplayName() + "'"), false);
                        return 1;
                    }
                    case FailedForUnknownReason -> source.sendFailure(Component.literal("Accepting failed for unknown reasons"));
                    case NoInvite -> source.sendFailure(Component.literal("You don't have any pending invites"));
                    case AlreadyInTeam -> source.sendFailure(Component.literal("You are already in a team"));
                    case TeamIsFull -> source.sendFailure(Component.literal("The team you tried to join is already full"));
                }

                return 0;
            })
        );


        // /team giveReward
        for (Reward rewardType : Reward.getRewardTypes()) {
            teamCommand.then(Commands.literal("giveReward")
                .requires(stack -> Team.get(Objects.requireNonNull(stack.getPlayer())).isPresent())
                .requires(stack -> stack.hasPermission(Commands.LEVEL_ADMINS))
                .then(Commands.argument("Player", EntityArgument.player())
                    .then(Commands.literal(rewardType.getTypeName())
                        .then(Commands.argument("rarity", EnumArgument.enumArgument(RandomItemsReward.Rarity.class))
                            .executes((context) -> {
                                var source = context.getSource();
                                var player = source.getPlayer();
                                assert player != null;
                                var team = Team.get(player);

                                if (team.isEmpty()) {
                                    source.sendFailure(Component.literal("This player is not part of a team"));
                                    return 0;
                                }

                                var rarity = context.getArgument("rarity", RandomItemsReward.Rarity.class);
                                team.get().addReward(rewardType.create(rarity));

                                source.sendSuccess(() -> Component.literal("Successfully gave the team of the Player '" + player.getDisplayName().getString() + "' a random item reward of the rarity '" + rarity + "'."), false);

                                return 1;
                            })
                        )
                    )
                )
            );
        }
        aeroCommand.then(teamCommand);


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
        int hours = Math.floorDiv(ticks, 72000);
        ticks -= hours * 72000;
        int minutes = Math.floorDiv(ticks, 1200);
        ticks -= minutes * 1200;
        int seconds = Math.floorDiv(ticks, 20);

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


    public static void sendMessage(UUID playerId, Component message) {sendMessage(Objects.requireNonNull(AerotechSMP.getServer().getPlayerList().getPlayer(playerId)), message);}


    public static void sendMessage(ServerPlayer player, Component message) {
        player.sendSystemMessage(
            message
        );
    }


    public static void sendError(UUID playerId, String error) {sendMessage(playerId, Component.literal(error).withColor(Color.red.getRGB()));}
}