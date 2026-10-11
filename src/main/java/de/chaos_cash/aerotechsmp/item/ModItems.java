package de.chaos_cash.aerotechsmp.item;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AerotechSMP.MOD_ID);




    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
