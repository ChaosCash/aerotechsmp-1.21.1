package de.chaos_cash.aerotechsmp.screen;


import de.chaos_cash.aerotechsmp.AerotechSMP;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, AerotechSMP.MOD_ID);


    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
