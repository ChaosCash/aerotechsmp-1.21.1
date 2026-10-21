package de.chaos_cash.aerotechsmp.phases;

import net.minecraft.world.BossEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class IntermissionPhase extends TimedPhase{

    public IntermissionPhase(int length, Phase nextPhase) {
        super(length, nextPhase);
    }

    public IntermissionPhase(int length, @NotNull Phase nextPhase, @NotNull Supplier<Boolean> lengtheningCondition, int lengthIncrease, @NotNull String lengtheningReason) {
        super(length, nextPhase, lengtheningCondition, lengthIncrease, lengtheningReason);
    }

    @Override
    public String getDisplayName() {
        return "Intermission";
    }

    @Override
    public BossEvent.BossBarColor getBossBarColor() {
        return BossEvent.BossBarColor.BLUE;
    }

    @SubscribeEvent
    @Override
    protected void onBossBarLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        super.onBossBarLogIn(event);
    }

    @SubscribeEvent
    @Override
    protected void onTick(ServerTickEvent.Post event) {
        super.onTick(event);
    }
}
