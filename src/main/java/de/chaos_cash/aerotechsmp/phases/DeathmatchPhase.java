package de.chaos_cash.aerotechsmp.phases;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.item.custom.HeartCoreItem;
import de.chaos_cash.aerotechsmp.team.Team;
import net.minecraft.world.level.border.WorldBorder;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.awt.*;

public class DeathmatchPhase extends PVPPhase{
    static final double borderBaseSpeedPerTick = .15;

    private double currentBorderSize = PeacefulPhase.borderSize;
    private final WorldBorder border;


    public DeathmatchPhase() {
        super(72000, new PeacefulPhase());

        border = AerotechSMP.getServer().overworld().getWorldBorder();
    }

    @Override
    public void onActivation() {
        super.onActivation();

        if (!AerotechSMP.getServer().overworld().isClientSide()) {
            giveCores();
        }
    }

    @Override
    public void onDeactivation() {
        super.onDeactivation();

        var server = AerotechSMP.getServer();
        if (!server.overworld().isClientSide()) {
            int leftOverTime = Math.max(length - activeDuration, 0);

            for (var player : server.getPlayerList().getPlayers()) {
                if (leftOverTime > 0)
                    HeartCoreItem.grantChargeProgress(player, leftOverTime);
                HeartCoreItem.grantAllRewards(player);
            }
        }
    }


    @Override
    public String getDisplayName() {
        return "Deathmatch-Phase";
    }


    @SubscribeEvent
    @Override
    protected void onTick(ServerTickEvent.Post event) {
        currentBorderSize = Math.max(currentBorderSize - getBorderShrinkSpeed(), 16);
        border.setSize(currentBorderSize);

        super.onTick(event);
    }


    public double getBorderShrinkSpeed() {
        double t = getProgress();
        return borderBaseSpeedPerTick * Math.pow(t + 1, -3.1);
    }


    public static void giveCores() {
        for (var player : AerotechSMP.getServer().getPlayerList().getPlayers()) {
            var optTeam = Team.get(player);
            if (Team.get(player).isEmpty()) continue;
            Team team = optTeam.get();


            player.getInventory().add(
                HeartCoreItem.newStack(team.getMaxTeamSize() / team.getOnlinePlayers().size())
            );
        }
    }


    @SubscribeEvent
    @Override
    protected void onBossBarLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        super.onBossBarLogIn(event);
    }
}
