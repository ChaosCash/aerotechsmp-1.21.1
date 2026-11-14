package de.chaos_cash.aerotechsmp.phases;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.util.ChatHandler;
import de.chaos_cash.aerotechsmp.datagen.Language.Messages;
import dev.architectury.event.CompoundEventResult;
import dev.ftb.mods.ftbchunks.api.ClaimResult;
import dev.ftb.mods.ftbchunks.api.ClaimedChunk;
import dev.ftb.mods.ftbchunks.api.event.ClaimedChunkEvent;
import net.creeperhost.ftbbackups.BackupHandler;
import net.creeperhost.ftbbackups.FTBBackups;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

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


    public static int schedulePVPPhaseStartCommand() {
        Messages.PvpImpeding.broadcast();

        createBackup("Pre PVP-Phase Backup ");

        setPhase(
            new IntermissionPhase(
                1200,
                new PVPPhase(60 * 60 * 20,10 * 60 * 20),
                BackupHandler::isRunning,
                400,
                "the Backup is not finished yet."
            )
        );

        return 1;
    }


    public static void createBackup(String name) {
        BackupHandler.createBackup(FTBBackups.minecraftServer, true, name);
    }


    private static CompoundEventResult<ClaimResult> onBeforeClaimChange(CommandSourceStack stack, ClaimedChunk claimedChunk) {
        if (phase.allowsTeamModifications())
            return CompoundEventResult.pass();
        else {
            Messages.TeamModificationsNotAllowedInPhase.send(stack.getPlayer());
            return CompoundEventResult.interruptTrue(ClaimResult.customProblem(Messages.TeamModificationsNotAllowedInPhase.key));
        }
    }



    @SubscribeEvent
    static void onServerStartup(ServerStartingEvent event) {
        setPhase(new PeacefulPhase());
        ClaimedChunkEvent.BEFORE_CLAIM.register(PhaseHandler::onBeforeClaimChange);
        ClaimedChunkEvent.BEFORE_UNCLAIM.register(PhaseHandler::onBeforeClaimChange);
    }
}
