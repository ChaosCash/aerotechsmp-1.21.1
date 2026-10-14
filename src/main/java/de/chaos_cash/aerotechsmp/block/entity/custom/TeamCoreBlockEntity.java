package de.chaos_cash.aerotechsmp.block.entity.custom;

import de.chaos_cash.aerotechsmp.block.custom.ArmorBlocks;
import de.chaos_cash.aerotechsmp.block.entity.ModBlockEntities;
import de.chaos_cash.aerotechsmp.team.Team;
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

import java.util.Optional;


public class TeamCoreBlockEntity extends ModBlockEntity {
    boolean isGivingRewards = false;


    public TeamCoreBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.TEAM_CORE_BLOCK_ENTITY.get(), pos, blockState);
    }



    public void dropRewards(Player player){
        Optional<Team> optionalTeam = Team.get(player);

        if (this.level == null) return;
        if (isGivingRewards) return;
        if (optionalTeam.isEmpty()) return;

        var team = optionalTeam.get();

        if (!team.hasRewards()) {
            level.playSound(null, getBlockPos(), SoundEvents.VAULT_INSERT_ITEM_FAIL, SoundSource.BLOCKS, 2, 1);
            return;
        }


        level.playSound(null, getBlockPos(), SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 3, 1);
        isGivingRewards = true;

        EventHandler.scheduleTask(40, () -> dropReward(team));
    }


    void dropReward(Team team){
        if (!team.hasRewards()) {
            finishGivingRewards();
        return;
        }
        if (!this.equals(this.level.getBlockEntity(this.worldPosition)))
            return; // if block is still placed

        level.playSound(null, getBlockPos(), SoundEvents.AMETHYST_BLOCK_PLACE, SoundSource.BLOCKS, 1, 1);

        var reward = team.getFirstReward();
        var items = reward.getRewards();

        for (var item : items) {
            Containers.dropItemStack(
                this.level,
                this.worldPosition.getX(),
                this.worldPosition.getY() + 1,
                this.worldPosition.getZ(),
                item
            );
        }
        team.removeReward(reward);

        if (team.hasRewards())
            EventHandler.scheduleTask(20, () -> dropReward(team));
            //dropReward(team);
        else
            //finishGivingRewards();
            EventHandler.scheduleTask(40, this::finishGivingRewards);
    }


    void finishGivingRewards(){
        isGivingRewards = false;
        level.playSound(null, getBlockPos(), SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS, 3, 1);
    }


    @Override
    protected void saveAdditionalData(CompoundTag tag, HolderLookup.Provider registries) {

    }


    @Override
    protected void loadAdditionalData(CompoundTag tag, HolderLookup.Provider registries) {

    }
}
