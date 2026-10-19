package de.chaos_cash.aerotechsmp.phases;

import com.mojang.brigadier.context.CommandContext;
import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.util.EventHandler;
import de.chaos_cash.aerotechsmp.util.ChatHandler;
import dev.architectury.event.CompoundEventResult;
import dev.ftb.mods.ftbchunks.FTBChunksAPIImpl;
import dev.ftb.mods.ftbchunks.api.ClaimResult;
import dev.ftb.mods.ftbchunks.api.ClaimedChunk;
import net.creeperhost.ftbbackups.BackupHandler;
import net.creeperhost.ftbbackups.FTBBackups;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = AerotechSMP.MOD_ID)
public class PhaseHandler {

    static Phase phase = Phase.Peaceful;

    static ServerBossEvent bossEvent;
    static final int PVP_PHASE_LENGTH = 1000;
    static int pvpPhaseActiveTicks = 0;



    public static Phase getPhase() {return phase;}



    public static int schedulePVPPhaseStartCommand(CommandContext<CommandSourceStack> context)
    {
        phase = Phase.Intermission;

        createBackup("Pre PVP-Phase Backup ");

        ChatHandler.broadcastCountdown(1200, "Starting PVP-Phase in ", "...", Component.literal("Starting PVP-Phase..."));

        EventHandler.scheduleTask(1200, () -> startPVPPhase(context.getSource()));

        return 1;
    }




    static void startPVPPhase(CommandSourceStack source)
    {
        phase = Phase.PVP;
        ChatHandler.broadcast(Component.literal("Starting PVP phase..."));

        deleteAllClaims(source);

        bossEvent = new ServerBossEvent(Component.literal("PVP-Phase"), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
        bossEvent.setVisible(true);
        for (var player : source.getServer().getPlayerList().getPlayers())
            bossEvent.addPlayer(player);
    }



    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (phase == Phase.PVP)
            onPVPPhaseTick(event);
    }



    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (phase == Phase.PVP) {
            var player = AerotechSMP.getServer().getPlayerList().getPlayer(event.getEntity().getUUID());
            if (player != null)
                bossEvent.addPlayer(player);
        }
    }



    public static void onPVPPhaseTick(ServerTickEvent.Post event) {
        pvpPhaseActiveTicks ++;
        bossEvent.setProgress(1 -(float) pvpPhaseActiveTicks / PVP_PHASE_LENGTH);
    }


    public static void createBackup()
    {
        createBackup("UnnamedBackup");
    }



    public static void createBackup(String name) {
        BackupHandler.createBackup(FTBBackups.minecraftServer, true, name);
    }



    static void deleteAllClaims(CommandSourceStack source)
    {
        ChatHandler.broadcast(Component.literal("Deleting all claims..."));

        var claimedChunks = FTBChunksAPIImpl.INSTANCE.getManager().getAllClaimedChunks().stream().toList();
        for (var chunk : claimedChunks)
        {
            chunk.unclaim(source, true);
        }

        ChatHandler.broadcast(Component.literal("Claims Deleted!"));
    }



    public static CompoundEventResult<ClaimResult> onChunkClaim(CommandSourceStack source, ClaimedChunk claimedChunk) {
        if (phase == Phase.PVP)
            claimedChunk.unclaim(source, true);

        return CompoundEventResult.interruptTrue(ClaimResult.success());
    }


    public enum Phase
    {
        Peaceful,
        PVP,
        Intermission
    }
}
