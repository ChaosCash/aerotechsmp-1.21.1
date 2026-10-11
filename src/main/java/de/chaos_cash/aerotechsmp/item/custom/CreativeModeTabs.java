package de.chaos_cash.aerotechsmp.item.custom;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.block.ModBlocks;
import de.chaos_cash.aerotechsmp.block.custom.ArmorBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AerotechSMP.MOD_ID);

    public static final Supplier<CreativeModeTab> ARMOR_BLOCKS_TAB = CREATIVE_MODE_TABS.register(
            "armor_blocks_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.literal("Armor Blocks"))
                    .icon(() -> ModBlocks.ARMOR_BLOCKS.get(ArmorBlocks.Tier.Compressed).get(ArmorBlocks.Color.Default).toStack())
                    .displayItems((itemDisplayParameters, output) -> {
                        for (ArmorBlocks.Color color : ArmorBlocks.Color.values())
                        {
                            for (ArmorBlocks.Tier tier : ArmorBlocks.Tier.values())
                            {
                                output.accept(ModBlocks.ARMOR_BLOCKS.get(tier).get(color).get());
                                output.accept(ModBlocks.ARMOR_STAIRS.get(tier).get(color).get());
                            }
                        }
                    }).build()
            );


    public static void register(IEventBus eventBus)
    {
        CREATIVE_MODE_TABS.register(eventBus);
    }

}
