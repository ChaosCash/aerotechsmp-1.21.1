package de.chaos_cash.aerotechsmp.phases;

public class PeacefulPhase extends Phase {
    @Override
    public String getDisplayName() {
        return "Peaceful-Phase";
    }

    @Override
    public boolean allowsTeamModifications() {
        return true;
    }
}
