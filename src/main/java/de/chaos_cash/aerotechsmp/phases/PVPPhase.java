package de.chaos_cash.aerotechsmp.phases;

import dev.ftb.mods.ftbchunks.FTBChunksWorldConfig;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public class PVPPhase extends TimedPhase{
    public PVPPhase(int length, Phase nextPhase) {
        super(length, nextPhase);
    }


    @Override
    public String getDisplayName() {
        return "PVP-Phase";
    }

    @Override
    public SoundEvent getActivationSound() {
        return SoundEvents.WITHER_SPAWN;
    }

    @Override
    public float getActivationSoundVolume() {
        return 0.75f;
    }


    @Override
    public void onActivation() {
        super.onActivation();

        FTBChunksWorldConfig.DISABLE_PROTECTION.set(true);
        FTBChunksWorldConfig.onConfigChanged(true);
    }

    @Override
    public void onDeactivation() {
        super.onDeactivation();

        FTBChunksWorldConfig.DISABLE_PROTECTION.set(false);
        FTBChunksWorldConfig.onConfigChanged(true);
    }


    @Override
    @SubscribeEvent
    protected void onTick(ServerTickEvent.Post event) {
        super.onTick(event);
    }
}
