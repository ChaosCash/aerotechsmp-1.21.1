package de.chaos_cash.aerotechsmp.block.entity;

import com.mojang.datafixers.types.Type;
import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.block.ModBlocks;
import de.chaos_cash.aerotechsmp.block.custom.TeamCoreBlock;
import de.chaos_cash.aerotechsmp.block.entity.custom.TeamCoreBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, AerotechSMP.MODID);


    public static final Supplier<BlockEntityType<TeamCoreBlockEntity>> TEAM_CORE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
            "team_core_be",
            () -> BlockEntityType.Builder.of(TeamCoreBlockEntity::new, ModBlocks.TEAM_CORE_BLOCK.get()).build(null)
    );



    public static void register(IEventBus eventBus)
    {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }

}
