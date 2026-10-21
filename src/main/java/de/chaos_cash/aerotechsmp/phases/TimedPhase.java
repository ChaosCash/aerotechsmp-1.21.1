package de.chaos_cash.aerotechsmp.phases;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.util.ChatHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;
import java.util.function.Supplier;

public abstract class TimedPhase extends Phase {
    private ServerBossEvent bossEvent;
    private int length;
    private int activeDuration;
    private final Phase nextPhase;
    private Supplier<Boolean> lengtheningCondition;
    private int lengthIncrease = 0;
    private String lengtheningReason = null;


    public TimedPhase(int length, @NotNull Phase nextPhase) {
        this.length = length;
        this.nextPhase = nextPhase;
    }

    public TimedPhase(int length, @NotNull Phase nextPhase, @NotNull Supplier<Boolean> lengtheningCondition, int lengthIncrease, @NotNull String lengtheningReason) {
        this(length, nextPhase);
        this.lengtheningCondition = lengtheningCondition;
        this.lengthIncrease = lengthIncrease;
        this.lengtheningReason = lengtheningReason;
    }

    public float getProgress() {return (float) activeDuration / length;}



    protected void onTimeUp(){
        if (lengtheningCondition != null && lengtheningCondition.get()) {
            length += lengthIncrease;
            ChatHandler.broadcast(
                Component.literal(getDisplayName() + " has been lengthened, because " + lengtheningReason)
                    .withColor(Color.CYAN.getRGB())
            );
        }
        else
            PhaseHandler.setPhase(nextPhase);
    }


    @Override
    public void onActivation() {
        super.onActivation();

        activeDuration = 0;

        bossEvent = new ServerBossEvent(Component.literal(getDisplayName()), getBossBarColor(), getBossBarOverlay());

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


    @Override
    public MutableComponent getActivationMessage() {
        return super.getActivationMessage().append(" (Estimated length: " + ChatHandler.formatTickTime(length) + ")");
    }
}
