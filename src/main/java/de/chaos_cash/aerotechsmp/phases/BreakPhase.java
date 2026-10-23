package de.chaos_cash.aerotechsmp.phases;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.jetbrains.annotations.NotNull;

public class BreakPhase extends TimedPhase{

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


    @Override
    public SoundEvent getActivationSound() {
        return SoundEvents.BEACON_ACTIVATE;
    }
}
