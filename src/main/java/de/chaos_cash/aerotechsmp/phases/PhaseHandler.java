package de.chaos_cash.aerotechsmp.phases;

import com.mojang.brigadier.context.CommandContext;
import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.util.ChatHandler;
import dev.ftb.mods.ftbchunks.api.ClaimedChunk;
import dev.ftb.mods.ftbchunks.api.event.ClaimedChunkEvent;
import net.creeperhost.ftbbackups.BackupHandler;
import net.creeperhost.ftbbackups.FTBBackups;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import java.awt.*;
import java.util.Objects;

@EventBusSubscriber(modid = AerotechSMP.MOD_ID)
public class PhaseHandler {

    static Phase phase;



    public static Phase getPhase() {return phase;}

    public static void setPhase(Phase newPhase) {
        if (phase != null)
            phase.onDeactivation();
        phase = newPhase;
        phase.onActivation();

        for (var player : AerotechSMP.getServer().getPlayerList().getPlayers())
            ChatHandler.updateCommands(player);
    }


    public static int schedulePVPPhaseStartCommand(CommandContext<CommandSourceStack> context)
    {
        ChatHandler.broadcast(Component.literal("PVP-Phase will start shortly!").withColor(Color.red.getRGB()));

        createBackup("Pre PVP-Phase Backup ");

        setPhase(
            new IntermissionPhase(
                1200,
                new DominationPhase(),
                BackupHandler::isRunning,
                400,
                "the Backup is not finished yet."
            )
        );

        return 1;
    }

    public static void createBackup()
    {
        createBackup("UnnamedBackup");
    }



    public static void createBackup(String name) {
        BackupHandler.createBackup(FTBBackups.minecraftServer, true, name);
    }


    @SubscribeEvent
    static void onServerStartup(ServerStartingEvent event) {
        setPhase(new PeacefulPhase());
        ClaimedChunkEvent.AFTER_CLAIM.register(PhaseHandler::onAfterClaim);
    }


    private static void onAfterClaim(CommandSourceStack commandSourceStack, ClaimedChunk claimedChunk) {
        if (phase == null || phase.allowsTeamModifications())
            return;

        claimedChunk.unclaim(commandSourceStack, true);
        ChatHandler.sendError(
            Objects.requireNonNull(commandSourceStack.getPlayer()).getUUID(),
            "In this Phase claiming is not allowed!"
        );
    }
}
