package de.chaos_cash.aerotechsmp.block.entity.custom;

import de.chaos_cash.aerotechsmp.block.entity.ModBlockEntities;
import de.chaos_cash.aerotechsmp.datagen.Language.MenuNames;
import de.chaos_cash.aerotechsmp.screen.custom.ShipProtectorMenu;
import dev.ryanhcode.sable.companion.SableCompanion;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ShipProtectorBlockEntity extends ModBlockEntity implements MenuProvider {
    static Map<UUID, ShipProtectorBlockEntity> protectors = new HashMap<>();
    SubLevelAccess subLevel;
    boolean isRegistered = false;
    EnergyStorage energyStorage = new EnergyStorage(1000000);

    public ShipProtectorBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.SHIP_PROTECTOR.get(), pos, blockState);
    }


    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }


    public float getExplosionResistanceBonus() {
        return (float) Math.random() * 250;
    }



    @Override
    public void onLoad() {
        super.onLoad();
        subLevel = SableCompanion.INSTANCE.getContaining(level, worldPosition);
        registerProtector();
    }


    void registerProtector() {
        if (subLevel != null && !protectors.containsKey(subLevel.getUniqueId())) {
            protectors.put(subLevel.getUniqueId(), this);
            isRegistered = true;
        }
    }

    void unregisterProtector() {
        if (isRegistered) {
            protectors.remove(subLevel.getUniqueId(), this);
            isRegistered = false;
        }
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        unregisterProtector();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        unregisterProtector();
    }

    public static Optional<ShipProtectorBlockEntity> getForSubLevel(UUID shipId) {
        return Optional.ofNullable(protectors.getOrDefault(shipId, null));
    }


    @Override
    protected void saveAdditionalData(CompoundTag tag, HolderLookup.Provider registries) {

    }

    @Override
    protected void loadAdditionalData(CompoundTag tag, HolderLookup.Provider registries) {

    }

    @Override
    public @NotNull Component getDisplayName() {
        return MenuNames.ShipProtector.getTranslation();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return new ShipProtectorMenu(containerId, inventory, this);
    }
}
