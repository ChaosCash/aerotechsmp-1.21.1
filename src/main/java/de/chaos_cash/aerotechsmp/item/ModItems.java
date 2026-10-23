package de.chaos_cash.aerotechsmp.item;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.item.custom.HeartCoreItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AerotechSMP.MOD_ID);

    public static final DeferredItem<HeartCoreItem> HEART_CORE = registerItem(
            "heart_core",
            () -> new HeartCoreItem(new Item.Properties().fireResistant().stacksTo(1))
    );




    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }


    public static <T extends Item> DeferredItem<T> registerItem(String name, Supplier<T> block)
    {
        return ITEMS.register(name, block);
    }
}
