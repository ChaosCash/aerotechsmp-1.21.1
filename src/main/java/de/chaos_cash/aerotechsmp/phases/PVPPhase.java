package de.chaos_cash.aerotechsmp.phases;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public abstract class PVPPhase extends TimedPhase{
    public PVPPhase(int length, Phase nextPhase) {
        super(length, nextPhase);
    }


    @Override
    public SoundEvent getActivationSound() {
        return SoundEvents.WITHER_SPAWN;
    }

    @Override
    public float getActivationSoundVolume() {
        return 0.75f;
    }
}
