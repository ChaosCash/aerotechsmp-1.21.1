package de.chaos_cash.aerotechsmp.phases;

import net.minecraft.network.chat.Component;
import net.minecraft.world.BossEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public class IntermissionPhase extends TimedPhase{

    public IntermissionPhase(int length, Phase nextPhase) {
        super(length, nextPhase);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Intermission");
    }

    @Override
    public BossEvent.BossBarColor getBossBarColor() {
        return BossEvent.BossBarColor.BLUE;
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
