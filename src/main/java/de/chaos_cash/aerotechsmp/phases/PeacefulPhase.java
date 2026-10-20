package de.chaos_cash.aerotechsmp.phases;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import net.minecraft.network.chat.Component;

public class PeacefulPhase extends Phase {
    public static final int borderSize = 4000;


    @Override
    public Component getDisplayName() {
        return Component.literal("Peaceful-Phase");
    }


    @Override
    public void onActivation() {
        super.onActivation();

        var border = AerotechSMP.getServer().overworld().getWorldBorder();
        border.setSize(PeacefulPhase.borderSize);
    }
}
