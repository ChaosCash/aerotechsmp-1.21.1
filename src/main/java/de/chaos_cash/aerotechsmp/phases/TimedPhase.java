package de.chaos_cash.aerotechsmp.phases;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class TimedPhase extends Phase {
    private ServerBossEvent bossEvent;
    private final int length;
    private int activeDuration;
    Phase nextPhase;


    public TimedPhase(int length, @NotNull Phase nextPhase) {
        this.length = length;
        this.nextPhase = nextPhase;
    }

    public float getProgress() {return (float) activeDuration / length;}



    protected void onTimeUp(){
        PhaseHandler.setPhase(nextPhase);
    }


    @Override
    public void onActivation() {
        super.onActivation();

        activeDuration = 0;

        bossEvent = new ServerBossEvent(getDisplayName(), getBossBarColor(), getBossBarOverlay());

        bossEvent.setVisible(true);
        for (var player : AerotechSMP.getServer().getPlayerList().getPlayers())
            bossEvent.addPlayer(player);

        NeoForge.EVENT_BUS.register(this);

    }

    @Override
    public void onDeactivation() {
        super.onDeactivation();

        NeoForge.EVENT_BUS.unregister(this);

        bossEvent.setVisible(false);
    }


    public BossEvent.BossBarColor getBossBarColor() {return BossEvent.BossBarColor.RED;}

    public BossEvent.BossBarOverlay getBossBarOverlay() {return BossEvent.BossBarOverlay.PROGRESS;}



    protected void onTick(ServerTickEvent.Post event) {
        activeDuration ++;

        bossEvent.setProgress(1 - getProgress());

        if (activeDuration >= length)
            onTimeUp();
    }

    protected void onBossBarLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        bossEvent.addPlayer(Objects.requireNonNull(AerotechSMP.getServer().getPlayerList().getPlayer(event.getEntity().getUUID())));
    }

}
