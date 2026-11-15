package de.chaos_cash.aerotechsmp.phases;

import de.chaos_cash.aerotechsmp.datagen.Language.Messages;
import de.chaos_cash.aerotechsmp.team.Team;
import dev.ftb.mods.ftbchunks.FTBChunksWorldConfig;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.List;

public class PVPPhase extends TimedPhase{
    private final List<Team> teamsWithCoreOnStart = new ArrayList<>();
    private boolean activatedOnce = false;

    public PVPPhase(int length, int breakLength) {
        super(length, null);
        nextPhase = new BreakPhase(breakLength, this);
    }



    public void onTeamCoreDestruction(Team coreTeam, Team destroyingTeam) {
        destroyingTeam.sendTeamMessage(Messages.EnemyTeamCoreDestroyed);
        coreTeam.sendTeamMessage(Messages.OwnTeamCoreDestroyed);
        destroyingTeam.grantCoreReward();
    }

    @Override
    protected void onTimeUp() {
        super.onTimeUp();

        activeDuration = 0;
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

        if (!activatedOnce) {
            for (var team : Team.list())
                if (team.hasCore() && !teamsWithCoreOnStart.contains(team))
                    teamsWithCoreOnStart.add(team);
            activatedOnce = true;
        }
        else if (PhaseHandler.getPhase() == nextPhase)
            nextPhase = new WaitForRestartPhase();
    }


    @Override
    public void onDeactivation() {
        super.onDeactivation();

        FTBChunksWorldConfig.DISABLE_PROTECTION.set(false);
        FTBChunksWorldConfig.onConfigChanged(true);

        if (!(nextPhase instanceof BreakPhase))
            for (var team : teamsWithCoreOnStart) {
                if (team.hasCore()) {
                    team.grantCoreReward();
                    team.sendTeamMessage(Messages.TeamCoreSurvived);
                }
                else
                    team.setHasCore(true);
            }
    }


    @Override
    @SubscribeEvent
    protected void onTick(ServerTickEvent.Post event) {
        super.onTick(event);
    }
}
