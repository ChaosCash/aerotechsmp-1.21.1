package de.chaos_cash.aerotechsmp.util;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddPackFindersEvent;

@EventBusSubscriber(modid = AerotechSMP.MOD_ID)
public class PackHandler {
    @SubscribeEvent
    public static void addPacks(AddPackFindersEvent event) {
        event.addPackFinders(
                ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID, "resourcepacks/recipe_overrides"),
                PackType.SERVER_DATA,
                Component.literal("AeroTech Recipe Overrides"),
                PackSource.BUILT_IN,
                true,
                Pack.Position.TOP
        );
    }
}
