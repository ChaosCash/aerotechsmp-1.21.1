package de.chaos_cash.aerotechsmp.item;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.item.custom.ClaimGrantingItem;
import de.chaos_cash.aerotechsmp.item.custom.ForceLoadGrantingItem;
import de.chaos_cash.aerotechsmp.item.custom.HeartCoreItem;
import de.chaos_cash.aerotechsmp.util.Messages;
import de.chaos_cash.aerotechsmp.util.Rarity;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AerotechSMP.MOD_ID);

    public static final DeferredItem<HeartCoreItem> HEART_CORE = registerItem(
            "heart_core",
            () -> new HeartCoreItem(new Item.Properties().fireResistant().stacksTo(1))
    );

    public static final DeferredItem<ClaimGrantingItem> CLAIM_SHARD = registerItem("claim_shard", () -> new ClaimGrantingItem(Messages.ClaimShardUsed, 1));
    public static final DeferredItem<ClaimGrantingItem> CLAIM_CAPSULE = registerItem("claim_capsule", () -> new ClaimGrantingItem(Messages.ClaimCapsuleUsed, 5));
    public static final DeferredItem<ClaimGrantingItem> CLAIM_ESSENCE = registerItem("claim_essence", () -> new ClaimGrantingItem(Messages.ClaimEssenceUsed, 25));

    public static final DeferredItem<ForceLoadGrantingItem> FORCE_LOAD_SHARD = registerItem("force_load_shard", () -> new ForceLoadGrantingItem(Messages.ForceLoadShardUsed, 1));
    public static final DeferredItem<ForceLoadGrantingItem> FORCE_LOAD_CAPSULE = registerItem("force_load_capsule", () -> new ForceLoadGrantingItem(Messages.ForceLoadCapsuleUsed, 3));
    public static final DeferredItem<ForceLoadGrantingItem> FORCE_LOAD_ESSENCE = registerItem("force_load_essence", () -> new ForceLoadGrantingItem(Messages.ForceLoadEssenceUsed, 9));

    public static final Map<Rarity, DeferredItem<Item>> COMPONENT_BASES = registerItemsWithTypes("component_base", Rarity.class, () -> new Item(new Item.Properties()));

    public static final Map<Rarity, DeferredItem<Item>> KINETIC_COMPONENTS = registerItemsWithTypes("kinetic_component", Rarity.class, () -> new Item(new Item.Properties()));
    public static final Map<Rarity, DeferredItem<Item>> ELECTRIC_COMPONENTS = registerItemsWithTypes("electric_component", Rarity.class, () -> new Item(new Item.Properties()));
    public static final Map<Rarity, DeferredItem<Item>> SPATIAL_COMPONENTS = registerItemsWithTypes("spatial_component", Rarity.class, () -> new Item(new Item.Properties()));

    public static final Map<Rarity, DeferredItem<Item>> COMPONENT_BOARDS = registerItemsWithTypes("component_board", Rarity.class, () -> new Item(new Item.Properties()));
    


    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }


    public static <T extends Item> DeferredItem<T> registerItem(String name, Supplier<T> item)
    {
        return ITEMS.register(name, item);
    }


    public static <T extends Enum<T>, K extends Item> Map<T, DeferredItem<K>> registerItemsWithTypes(String id, Class<T> typesClass, Supplier<K> item) {
        var map = new EnumMap<T, DeferredItem<K>>(typesClass);
        for (var type : EnumSet.allOf(typesClass)){
            map.put(type, registerItem(id + "_" + type.name().toLowerCase(), item));
        }

        return map;
    }
}
