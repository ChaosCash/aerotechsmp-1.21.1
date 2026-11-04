package de.chaos_cash.aerotechsmp.item;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.item.custom.ClaimGrantingItem;
import de.chaos_cash.aerotechsmp.item.custom.ForceLoadGrantingItem;
import de.chaos_cash.aerotechsmp.team.Team;
import de.chaos_cash.aerotechsmp.util.Messages;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AerotechSMP.MOD_ID);

    public static final DeferredItem<ClaimGrantingItem> CLAIM_SHARD = registerItem("claim_shard", () -> new ClaimGrantingItem(Messages.ClaimShardUsed, 1, 25));
    public static final DeferredItem<ClaimGrantingItem> CLAIM_CAPSULE = registerItem("claim_capsule", () -> new ClaimGrantingItem(Messages.ClaimCapsuleUsed, 2, 75));
    public static final DeferredItem<ClaimGrantingItem> CLAIM_ESSENCE = registerItem("claim_essence", () -> new ClaimGrantingItem(Messages.ClaimEssenceUsed, 5, Team.hardMaxClaims));

    public static final DeferredItem<ForceLoadGrantingItem> FORCE_LOAD_SHARD = registerItem("force_load_shard", () -> new ForceLoadGrantingItem(Messages.ForceLoadShardUsed, 1, 5));
    public static final DeferredItem<ForceLoadGrantingItem> FORCE_LOAD_CAPSULE = registerItem("force_load_capsule", () -> new ForceLoadGrantingItem(Messages.ForceLoadCapsuleUsed, 1, 10));
    public static final DeferredItem<ForceLoadGrantingItem> FORCE_LOAD_ESSENCE = registerItem("force_load_essence", () -> new ForceLoadGrantingItem(Messages.ForceLoadEssenceUsed, 1, 16));



    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }


    public static <T extends Item> DeferredItem<T> registerItem(String name, Supplier<T> item)
    {
        return ITEMS.register(name, item);
    }
}
