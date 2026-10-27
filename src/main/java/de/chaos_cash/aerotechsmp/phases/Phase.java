package de.chaos_cash.aerotechsmp.phases;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.util.ChatHandler;
import de.chaos_cash.aerotechsmp.util.Messages;
import dev.architectury.event.CompoundEventResult;
import dev.ftb.mods.ftbchunks.api.ClaimResult;
import dev.ftb.mods.ftbchunks.api.ClaimedChunk;
import dev.ftb.mods.ftbchunks.api.event.ClaimedChunkEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

import java.awt.*;

public abstract class Phase {
    public void onActivation() {
        ChatHandler.broadcast(getActivationMessage());

        var activationSound = getActivationSound();
        if (activationSound != null)
            for (var player : AerotechSMP.getServer().getPlayerList().getPlayers())
                player.playNotifySound(activationSound, SoundSource.AMBIENT, getActivationSoundVolume(), 1);

        ClaimedChunkEvent.BEFORE_CLAIM.register(this::onBeforeClaim);
    }

    protected CompoundEventResult<ClaimResult> onBeforeClaim(CommandSourceStack stack, ClaimedChunk claimedChunk) {
        if (allowsTeamModifications())
            return CompoundEventResult.pass();
        else {
            Messages.TeamModificationsNotAllowedInPhase.send(stack.getPlayer());
            return CompoundEventResult.interruptTrue(ClaimResult.customProblem(Messages.TeamModificationsNotAllowedInPhase.key));
        }
    }


    public void onDeactivation() {
        ClaimedChunkEvent.BEFORE_CLAIM.unregister(this::onBeforeClaim);
    }


    public boolean allowsTeamModifications() {return false;}

    public MutableComponent getActivationMessage() {
        return Messages.StartingPhase.getTranslation().append(getDisplayName());
    }



    public abstract String getDisplayName();

    public SoundEvent getActivationSound() {return null;}

    public float getActivationSoundVolume() {return 1f;}

}
