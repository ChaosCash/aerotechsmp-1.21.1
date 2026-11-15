package de.chaos_cash.aerotechsmp.phases;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.datagen.Language.Messages;
import de.chaos_cash.aerotechsmp.util.ChatHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class BreakPhase extends TimedPhase {
    Map<ServerPlayer, Vec3> playerPositions = new HashMap<>();

    public BreakPhase(int length, @NotNull Phase nextPhase) {
        super(length, nextPhase);
    }

    @Override
    public String getDisplayName() {
        return "Break";
    }

    @Override
    public BossEvent.BossBarColor getBossBarColor() {
        return BossEvent.BossBarColor.BLUE;
    }


    @Override
    public void onActivation() {
        super.onActivation();

        var server = AerotechSMP.getServer();
        server.tickRateManager().setFrozen(true);

        for (var player : server.getPlayerList().getPlayers())
            playerPositions.putIfAbsent(player, player.position());

    }

    @Override
    public void onDeactivation() {
        super.onDeactivation();

        AerotechSMP.getServer().tickRateManager().setFrozen(false);
    }

    @SubscribeEvent
    @Override
    protected void onBossBarLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        super.onBossBarLogIn(event);
    }

    @SubscribeEvent()
    @Override
    protected void onTick(ServerTickEvent.Post event) {
        for (var entry : playerPositions.entrySet()) {
            var player = entry.getKey();
            if (!entry.getKey().hasDisconnected()) {
                var pos = entry.getValue();
                player.teleportTo(pos.x, pos.y, pos.z);

                ChatHandler.showTitle(
                        player,
                        Messages.GameIsPaused.getTranslation(),
                        Component.literal(ChatHandler.formatTickTime(length - activeDuration, true))
                );
            }
            else
                playerPositions.remove(player);
        }


        super.onTick(event);
    }

    @SubscribeEvent
    private void onBlockBreak(BlockEvent.BreakEvent event) {
        event.setCanceled(true);
    }

    @SubscribeEvent
    private void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        event.setCanceled(true);
    }

    @SubscribeEvent
    private void onAttackEntity(AttackEntityEvent event) {
        event.setCanceled(true);
    }


    @Override
    public SoundEvent getActivationSound() {
        return SoundEvents.BEACON_ACTIVATE;
    }

}
