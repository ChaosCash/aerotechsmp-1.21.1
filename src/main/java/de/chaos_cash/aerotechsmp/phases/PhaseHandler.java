package de.chaos_cash.aerotechsmp.phases;

import com.mojang.brigadier.context.CommandContext;
import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.util.ChatHandler;
import net.creeperhost.ftbbackups.BackupHandler;
import net.creeperhost.ftbbackups.FTBBackups;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
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
    }


    public static int schedulePVPPhaseStartCommand(CommandContext<CommandSourceStack> context)
    {
        setPhase(new IntermissionPhase(1200, new DominationPhase()));

        createBackup("Pre PVP-Phase Backup ");

        ChatHandler.broadcastCountdown(1200, "Starting PVP-Phase in ", "...", Component.literal("Starting PVP-Phase..."));

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
    }
}
