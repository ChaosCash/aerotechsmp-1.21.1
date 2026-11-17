package de.chaos_cash.aerotechsmp.phases;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.datagen.Language.Messages;
import de.chaos_cash.aerotechsmp.team.Team;
import dev.ftb.mods.ftbchunks.FTBChunksWorldConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.GameType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
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


        for (var player : AerotechSMP.getServer().getPlayerList().getPlayers())
            checkPlayerTeamRequirements(player);
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


    protected void checkPlayerTeamRequirements(ServerPlayer player) {
        var optTeam = Team.get(player);
        if (optTeam.isEmpty())
            player.connection.disconnect(Messages.PhaseRequiresTeam.getTranslation());
        else if (!teamsWithCoreOnStart.contains(optTeam.get()))
            player.connection.disconnect(Messages.PhaseRequiresTeamCore.getTranslation());
    }


    @SubscribeEvent
    protected void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        var player = AerotechSMP.getServer().getPlayerList().getPlayer(event.getEntity().getUUID());
        if (player == null) return;

        var optTeam = Team.get(player);
        if (optTeam.isPresent())
            if (!optTeam.get().hasCore())
                player.setGameMode(GameType.SPECTATOR);
    }


    @SubscribeEvent
    protected void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        var player = AerotechSMP.getServer().getPlayerList().getPlayer(event.getEntity().getUUID());
        assert player != null;
        var optTeam = Team.get(player);
        if (optTeam.isEmpty())
            player.connection.disconnect(Messages.PhaseRequiresTeam.getTranslation());
        else if (!optTeam.get().hasCore())
            player.connection.disconnect(Messages.PhaseRequiresTeamCore.getTranslation());

    }


    @Override
    @SubscribeEvent
    protected void onTick(ServerTickEvent.Post event) {
        super.onTick(event);
    }
}
