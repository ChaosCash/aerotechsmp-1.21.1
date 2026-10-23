package de.chaos_cash.aerotechsmp.phases;

import de.chaos_cash.aerotechsmp.AerotechSMP;

public class PeacefulPhase extends Phase {
    public static final int borderSize = 4000;


    @Override
    public String getDisplayName() {
        return "Peaceful-Phase";
    }

    @Override
    public boolean allowsTeamModifications() {
        return true;
    }


    @Override
    public void onActivation() {
        super.onActivation();

        var border = AerotechSMP.getServer().overworld().getWorldBorder();
        border.setSize(PeacefulPhase.borderSize);
    }
}
