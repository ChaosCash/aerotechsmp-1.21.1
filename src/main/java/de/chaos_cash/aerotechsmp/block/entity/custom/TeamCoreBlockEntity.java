package de.chaos_cash.aerotechsmp.block.entity.custom;

import de.chaos_cash.aerotechsmp.block.custom.ArmorBlocks;
import de.chaos_cash.aerotechsmp.block.entity.ModBlockEntities;
import de.chaos_cash.aerotechsmp.chat.ChatHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TeamCoreBlockEntity extends ModBlockEntity {
    public final ItemStackHandler inventory = new ItemStackHandler(100);


    public TeamCoreBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.TEAM_CORE_BLOCK_ENTITY.get(), pos, blockState);


    }



    public void dropItems() {
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for(int i = 0; i < inventory.getSlots(); i++) {
            inv.setItem(i, inventory.getStackInSlot(i));
        }

        if (this.level != null)
            Containers.dropContents(this.level, this.worldPosition, inv);
    }


    @Override
    protected void saveAdditionalData(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("inventory", inventory.serializeNBT(registries));
    }


    @Override
    protected void loadAdditionalData(CompoundTag tag, HolderLookup.Provider registries) {
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
    }
}
