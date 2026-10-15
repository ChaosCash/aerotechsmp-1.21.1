package de.chaos_cash.aerotechsmp.block.entity.custom;

import de.chaos_cash.aerotechsmp.block.entity.ModBlockEntities;
import de.chaos_cash.aerotechsmp.team.Team;
import de.chaos_cash.aerotechsmp.util.ChatHandler;
import de.chaos_cash.aerotechsmp.util.EventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;

import java.awt.*;
import java.util.Optional;


public class TeamCoreBlockEntity extends ModBlockEntity {
    boolean isGivingRewards = false;
    private Player placer;

    private Team team;


    public TeamCoreBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.TEAM_CORE_BLOCK_ENTITY.get(), pos, blockState);

        EventHandler.scheduleTask(1, this::checkPlacer);
    }


    public void setPlacer(Player placer) {this.placer = placer;}

    private void checkPlacer() {
        if (level == null){
            return;
        }
        if (level.isClientSide) return;

        if (placer == null) {
            level.destroyBlock(getBlockPos(), true);
            return;
        }

        var team = Team.get(placer);
        if (team.isEmpty()) {
            level.destroyBlock(getBlockPos(), true);
            ChatHandler.sendMessage(placer, Component.literal("You need to be part of a team to place a Team Core!").withColor(Color.red.getRGB()));
            return;
        }

        this.team = team.get();
    }



    public void dropRewards(Player player){
        var playerTeam = Team.get(player);
        if (playerTeam.isEmpty() || playerTeam.get() != team){
            ChatHandler.sendMessage(player, Component.literal("This team Core is not owned by Your Team!").withColor(Color.red.getRGB()));
            return;
        }

        if (this.level == null) return;
        if (isGivingRewards) return;
        if (team == null) return;

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
