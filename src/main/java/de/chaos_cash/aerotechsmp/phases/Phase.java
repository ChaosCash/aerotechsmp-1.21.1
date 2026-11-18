package de.chaos_cash.aerotechsmp.phases;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.Config;
import de.chaos_cash.aerotechsmp.util.ChatHandler;
import de.chaos_cash.aerotechsmp.datagen.Language.Messages;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public abstract class Phase {
    public void onActivation() {
        ChatHandler.broadcast(getActivationMessage());

        var activationSound = getActivationSound();
        if (activationSound != null)
            for (var player : AerotechSMP.getServer().getPlayerList().getPlayers())
                player.playNotifySound(activationSound, SoundSource.AMBIENT, getActivationSoundVolume(), 1);

        var border = AerotechSMP.getServer().overworld().getWorldBorder();
        border.setSize(getBorderDiameter());
    }

    protected int getBorderDiameter() {
        return Config.BORDER_DIAMETER.get();
    }


    public void onDeactivation() {
    }


    public boolean allowsTeamModifications() {return false;}

    public MutableComponent getActivationMessage() {
        return Messages.StartingPhase.getTranslation().append(getDisplayName());
    }



    public abstract String getDisplayName();

    public SoundEvent getActivationSound() {return null;}

    public float getActivationSoundVolume() {return 1f;}

}
