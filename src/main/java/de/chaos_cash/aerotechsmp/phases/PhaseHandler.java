package de.chaos_cash.aerotechsmp.phases;

import com.mojang.brigadier.context.CommandContext;
import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.util.EventHandler;
import de.chaos_cash.aerotechsmp.util.ChatHandler;
import dev.ftb.mods.ftbchunks.FTBChunksAPIImpl;
import dev.ftb.mods.ftbchunks.api.ClaimedChunk;
import net.creeperhost.ftbbackups.BackupHandler;
import net.creeperhost.ftbbackups.FTBBackups;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.level.border.WorldBorder;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = AerotechSMP.MOD_ID)
public class PhaseHandler {

    static Phase phase = Phase.Peaceful;

    static ServerBossEvent bossEvent;
    static final int pvpPhaseLength = 144000 / 60;
    static final int battleRoyalLength = pvpPhaseLength / 2;
    static final int battleRoyalStart = pvpPhaseLength - battleRoyalLength;
    static int pvpPhaseActiveTicks = 0;

    static int defaultBorderSize = 4000;
    static double currentBorderSize = defaultBorderSize;
    static double borderBaseSpeedPerTick = .15 * 60;
    private static WorldBorder border;


    @SubscribeEvent
    public static void onServerStart(ServerStartingEvent event){
        border = AerotechSMP.getServer().overworld().getWorldBorder();
        border.setSize(currentBorderSize);
    }



    public static Phase getPhase() {return phase;}



    public static int schedulePVPPhaseStartCommand(CommandContext<CommandSourceStack> context)
    {
        phase = Phase.Intermission;

        createBackup("Pre PVP-Phase Backup ");

        ChatHandler.broadcastCountdown(1200, "Starting PVP-Phase in ", "...", Component.literal("Starting PVP-Phase..."));

        EventHandler.scheduleTask(0, () -> startPVPPhase(context.getSource()));

        return 1;
    }




    static void startPVPPhase(CommandSourceStack source)
    {
        phase = Phase.Domination;
        ChatHandler.broadcast(Component.literal("Starting PVP phase..."));

        deleteAllClaims(source);

        bossEvent = new ServerBossEvent(Component.literal("PVP-Phase"), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
        bossEvent.setVisible(true);
        for (var player : source.getServer().getPlayerList().getPlayers())
            bossEvent.addPlayer(player);
    }



    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (phase == Phase.Domination || phase == Phase.BattleRoyal)
            onPVPPhaseTick(event);
    }



    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (phase == Phase.Domination) {
            var player = AerotechSMP.getServer().getPlayerList().getPlayer(event.getEntity().getUUID());
            if (player != null)
                bossEvent.addPlayer(player);
        }
    }



    public static void onPVPPhaseTick(ServerTickEvent.Post event) {
        pvpPhaseActiveTicks ++;
        bossEvent.setProgress(1 - ((float) pvpPhaseActiveTicks / pvpPhaseLength));

        if (phase == Phase.Domination){
            if (pvpPhaseActiveTicks > battleRoyalStart)
                phase = Phase.BattleRoyal;
        }
        else {
            currentBorderSize = Math.max(currentBorderSize - getBorderShrinkSpeed(), 16);
            border.setSize(currentBorderSize);
        }
    }


    public static double getBorderShrinkSpeed(){
        double t = (double) getBattleRoyalActiveTicks() / battleRoyalLength;
        return borderBaseSpeedPerTick * Math.pow(t + 1, -3.1);
    }


    public static int getBattleRoyalActiveTicks(){
        return Math.max(pvpPhaseActiveTicks - battleRoyalStart, 0);
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



    public static void onChunkClaim(CommandSourceStack source, ClaimedChunk claimedChunk) {
        if (phase == Phase.Domination)
            claimedChunk.unclaim(source, true);
    }


    public enum Phase
    {
        Peaceful,
        Intermission,
        Domination,
        BattleRoyal,
    }
}
