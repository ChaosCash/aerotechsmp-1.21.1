package de.chaos_cash.aerotechsmp.item;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;

public class ModItemProperties {

    public static void addCustomItemProperties() {
        ItemProperties.register(
            ModItems.HEART_CORE.get(),
            ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID, "rarity"),

            (itemStack, clientLevel, livingEntity, i) -> {
                var customData = itemStack.get(DataComponents.CUSTOM_DATA);
                if (customData == null)
                    return 0;
                var tag = customData.copyTag();
                if (tag.contains("rarity"))
                    return tag.getInt("rarity");
                else
                    return 0;
            }
        );
    }
}
