package de.chaos_cash.aerotechsmp.block.entity.custom;

import de.chaos_cash.aerotechsmp.block.custom.ArmorBlocks;
import de.chaos_cash.aerotechsmp.block.entity.ModBlockEntities;
import de.chaos_cash.aerotechsmp.util.EventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;



public class TeamCoreBlockEntity extends ModBlockEntity {
    public final ItemStackHandler inventory = new ItemStackHandler(100);
    boolean isGivingRewards = false;


    public TeamCoreBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.TEAM_CORE_BLOCK_ENTITY.get(), pos, blockState);

        inventory.insertItem(0, ArmorBlocks.getBlock(ArmorBlocks.Tier.Light, ArmorBlocks.Color.Default).toStack(64), false);
        inventory.insertItem(1, ArmorBlocks.getBlock(ArmorBlocks.Tier.Light, ArmorBlocks.Color.Default).toStack(64), false);
        inventory.insertItem(2, ArmorBlocks.getBlock(ArmorBlocks.Tier.Light, ArmorBlocks.Color.Default).toStack(64), false);
        inventory.insertItem(3, ArmorBlocks.getBlock(ArmorBlocks.Tier.Light, ArmorBlocks.Color.Default).toStack(64), false);
    }



    public void dropRewards(Player player){
        if (this.level == null)
            return;

        SimpleContainer inv = getContainer();

        if (inv.isEmpty() || isGivingRewards) {
            level.playSound(null, getBlockPos(), SoundEvents.VAULT_INSERT_ITEM_FAIL, SoundSource.BLOCKS, 2, 1);
            return;
        }


        level.playSound(null, getBlockPos(), SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 3, 1);
        isGivingRewards = true;

        int curDelay = 40;
        for(var item : inv.getItems()) {
            if (item.isEmpty())
                continue;
            EventHandler.scheduleTask(curDelay, () -> dropReward(item));
            curDelay += 20;
        }

        EventHandler.scheduleTask(curDelay + 20, this::finishGivingRewards);
    }


    void dropReward(ItemStack item){
        if (item.isEmpty())
            return;

        level.playSound(null, getBlockPos(), SoundEvents.AMETHYST_BLOCK_PLACE, SoundSource.BLOCKS, 1, 1);

        Containers.dropItemStack(
            this.level,
            this.worldPosition.getX(),
            this.worldPosition.getY() + 1,
            this.worldPosition.getZ(),
            item
        );
    }


    void finishGivingRewards(){
        isGivingRewards = false;
        level.playSound(null, getBlockPos(), SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS, 3, 1);
    }



    public void dropItems() {
        SimpleContainer inv = getContainer();

        if (this.level != null)
            Containers.dropContents(this.level, this.worldPosition, inv);
    }


    public SimpleContainer getContainer(){
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for(int i = 0; i < inventory.getSlots(); i++) {
            inv.setItem(i, inventory.getStackInSlot(i));
        }

        return inv;
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
