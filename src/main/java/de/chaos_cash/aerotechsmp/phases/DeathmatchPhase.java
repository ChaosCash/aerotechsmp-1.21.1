package de.chaos_cash.aerotechsmp.phases;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import dev.ftb.mods.ftbchunks.api.ClaimedChunk;
import dev.ftb.mods.ftbchunks.api.event.ClaimedChunkEvent;
import net.minecraft.commands.CommandSourceStack;
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

        ClaimedChunkEvent.AFTER_CLAIM.register(this::onChunkClaim);
    }

    @Override
    public void onDeactivation() {
        super.onDeactivation();

        ClaimedChunkEvent.AFTER_CLAIM.unregister(this::onChunkClaim);
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


    public double getBorderShrinkSpeed(){
        double t = getProgress();
        return borderBaseSpeedPerTick * Math.pow(t + 1, -3.1);
    }


    private void onChunkClaim(CommandSourceStack source, ClaimedChunk claimedChunk) {
        claimedChunk.unclaim(source, true);
    }


    @SubscribeEvent
    @Override
    protected void onBossBarLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        super.onBossBarLogIn(event);
    }
}
