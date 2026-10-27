package de.chaos_cash.aerotechsmp.block.entity.custom;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.block.entity.ModBlockEntities;
import de.chaos_cash.aerotechsmp.phases.PeacefulPhase;
import de.chaos_cash.aerotechsmp.phases.PhaseHandler;
import de.chaos_cash.aerotechsmp.team.Team;
import de.chaos_cash.aerotechsmp.util.EventHandler;
import de.chaos_cash.aerotechsmp.util.Messages;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;


public class TeamCoreBlockEntity extends ModBlockEntity {
    boolean isGivingRewards = false;
    private Player placer;

    private Team team;
    private boolean isLoading = false;


    public TeamCoreBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.TEAM_CORE_BLOCK_ENTITY.get(), pos, blockState);

        EventHandler.scheduleTask(1, this::checkPlacer);
    }


    public Team getTeam() {return team;}


    public void setPlacer(Player placer) {this.placer = placer;}

    private void checkPlacer() {
        if (isLoading) return;
        if (level == null) return;
        if (level.isClientSide) return;
        if (placer == null){
            level.destroyBlock(getBlockPos(), true);
            return;
        }
        if (!PhaseHandler.getPhase().allowsTeamModifications()){
            Messages.TeamModificationsNotAllowedInPhase.send(placer);
            level.destroyBlock(getBlockPos(), true);
            return;
        }

        if (!level.dimensionType().equals(AerotechSMP.getServer().overworld().dimensionType())) {
            Messages.TeamCoreForbiddenOutsideOverworld.send(placer);
            level.destroyBlock(getBlockPos(), true);
            return;
        }

        var team = Team.get(placer);
        if (team.isEmpty()) {
            Messages.ActionRequiresTeam.send(placer);
            level.destroyBlock(getBlockPos(), true);
            return;
        }

        setTeam(team.get());
        if (this.team == null)
            Messages.TeamCoreAlreadyPresent.send(placer);
    }


    private void setTeam(Team team){
        if (team.setCore(this)){
            this.team = team;
        }
        else
            AerotechSMP.getServer().overworld().destroyBlock(getBlockPos(), true);
    }



    public void dropRewards(Player player){
        var playerTeam = Team.get(player);
        if (this.level == null) return;

        if (isGivingRewards){
            level.playSound(null, getBlockPos(), SoundEvents.VAULT_INSERT_ITEM_FAIL, SoundSource.BLOCKS, 2, 1);
            return;
        }

        if (playerTeam.isEmpty() || playerTeam.get() != team){
            level.playSound(null, getBlockPos(), SoundEvents.VAULT_INSERT_ITEM_FAIL, SoundSource.BLOCKS, 2, 1);
            Messages.TeamCoreNotOwned.send(player);
            return;
        }

        if (!(PhaseHandler.getPhase() instanceof PeacefulPhase)){
            level.playSound(null, getBlockPos(), SoundEvents.VAULT_INSERT_ITEM_FAIL, SoundSource.BLOCKS, 2, 1);
            Messages.RewardsInaccessibleInPhase.send(player);
            return;
        }

        if (!team.hasRewards()) {
            level.playSound(null, getBlockPos(), SoundEvents.VAULT_INSERT_ITEM_FAIL, SoundSource.BLOCKS, 2, 1);
            return;
        }


        level.playSound(null, getBlockPos(), SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 3, 1);
        isGivingRewards = true;

        EventHandler.scheduleTask(40, () -> dropReward(team));
    }


    void dropReward(Team team){
        if (this.level == null) return;

        if (!team.hasRewards()) {
            finishGivingRewards();
        return;
        }
        if (!this.equals(this.level.getBlockEntity(this.worldPosition)))
            return; // if block is still placed

        level.playSound(null, getBlockPos(), SoundEvents.AMETHYST_BLOCK_PLACE, SoundSource.BLOCKS, 1, 1);

        var reward = team.getFirstReward();
        var items = reward.getRewards(team);

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
        else
            EventHandler.scheduleTask(40, this::finishGivingRewards);
    }


    void finishGivingRewards(){
        isGivingRewards = false;

        if (this.level == null) return;
        level.playSound(null, getBlockPos(), SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS, 3, 1);
    }


    @Override
    protected void saveAdditionalData(CompoundTag tag, HolderLookup.Provider registries) {
        if (team != null)
            tag.putUUID("team_id", team.getId());
    }


    @Override
    protected void loadAdditionalData(CompoundTag tag, HolderLookup.Provider registries) {
        if (tag.hasUUID("team_id"))
            Team.get(tag.getUUID("team_id")).ifPresent(value -> team = value);

        if (team == null && level != null)
            EventHandler.scheduleTask(1, () -> this.level.destroyBlock(getBlockPos(), true));

        isLoading = true;
    }

    public void onRemove() {
        if (team != null)
            team.removeCore();
    }
}
