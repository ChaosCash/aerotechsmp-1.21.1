package de.chaos_cash.aerotechsmp.phases;

import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.NeoForge;

public abstract class Phase {
    public void onActivation() {}


    public void onDeactivation() {}



    public abstract Component getDisplayName();
}
