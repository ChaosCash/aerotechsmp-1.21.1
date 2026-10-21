package de.chaos_cash.aerotechsmp.phases;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.util.ChatHandler;
import dev.ftb.mods.ftbchunks.FTBChunksAPIImpl;
import dev.ftb.mods.ftbchunks.api.ClaimedChunk;
import dev.ftb.mods.ftbchunks.api.event.ClaimedChunkEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.awt.*;

public class DominationPhase extends TimedPhase{
    public DominationPhase() {
        super(72000, new DeathmatchPhase());
    }



    @Override
    public void onActivation() {
        super.onActivation();

        deleteAllClaims();

        ClaimedChunkEvent.AFTER_CLAIM.register(this::onChunkClaim);
    }

    @Override
    public void onDeactivation() {
        super.onDeactivation();

        ClaimedChunkEvent.AFTER_CLAIM.unregister(this::onChunkClaim);
    }

    @Override
    public String getDisplayName() {
        return "Domination-Phase";
    }


    static void deleteAllClaims()
    {
        ChatHandler.broadcast(Component.literal("Deleting all claims..."));

        var claimedChunks = FTBChunksAPIImpl.INSTANCE.getManager().getAllClaimedChunks().stream().toList();
        var stack = AerotechSMP.getServer().createCommandSourceStack();
        for (var chunk : claimedChunks)
        {
            chunk.unclaim(stack, true);
        }

        ChatHandler.broadcast(Component.literal("Claims Deleted!"));
    }


    private void onChunkClaim(CommandSourceStack source, ClaimedChunk claimedChunk) {
        claimedChunk.unclaim(source, true);
    }



    @SubscribeEvent
    @Override
    protected void onBossBarLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        super.onBossBarLogIn(event);
    }

    @SubscribeEvent
    @Override
    protected void onTick(ServerTickEvent.Post event) {
        super.onTick(event);
    }
}
