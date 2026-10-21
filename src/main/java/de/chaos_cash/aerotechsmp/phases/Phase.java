package de.chaos_cash.aerotechsmp.phases;

import de.chaos_cash.aerotechsmp.util.ChatHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.awt.*;

public abstract class Phase {
    public void onActivation() {
        ChatHandler.broadcast(getActivationMessage());
    }


    public void onDeactivation() {}

    public boolean allowsClaims() {return false;}

    public MutableComponent getActivationMessage() {
        return Component.literal("Starting " + getDisplayName() + "!").withColor(Color.YELLOW.getRGB());
    }



    public abstract String getDisplayName();
}
