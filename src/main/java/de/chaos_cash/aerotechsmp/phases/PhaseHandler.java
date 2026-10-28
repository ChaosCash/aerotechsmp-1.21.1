package de.chaos_cash.aerotechsmp.phases;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.util.ChatHandler;
import de.chaos_cash.aerotechsmp.util.Messages;
import net.creeperhost.ftbbackups.BackupHandler;
import net.creeperhost.ftbbackups.FTBBackups;
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
                new DominationPhase(),
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


    @SubscribeEvent
    static void onServerStartup(ServerStartingEvent event) {
        setPhase(new PeacefulPhase());
    }
}
