package de.chaos_cash.aerotechsmp.block.entity.custom;

import de.chaos_cash.aerotechsmp.block.ModBlocks;
import de.chaos_cash.aerotechsmp.block.entity.ModBlockEntities;
import de.chaos_cash.aerotechsmp.datagen.Language.Messages;
import de.chaos_cash.aerotechsmp.team.Team;
import dev.ftb.mods.ftbchunks.api.ClaimedChunk;
import dev.ftb.mods.ftbchunks.api.event.ClaimedChunkEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;


public class TeamCoreBlockEntity extends ClaimBlockEntity {
    ClaimedChunkEvent.After unLoadListener = this::onForceLoadChange;

    public TeamCoreBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.TEAM_CORE.get(), pos, blockState);
    }

    private void onForceLoadChange(CommandSourceStack stack, ClaimedChunk claimedChunk) {
        if (claimedChunk.getPos().chunkPos().toLong() == new ChunkPos(getBlockPos()).toLong()) {
            destroy(Messages.TeamCoreBrokenDueToUnclaiming);
            var player = stack.getPlayer();
            if (player != null)
                if (level != null)
                    if (!level.isClientSide)
                        player.addItem(ModBlocks.TEAM_CORE.toStack());
        }
    }

    @Override
    protected void onTeamChange(@Nullable Team oldTeam, @Nullable Team newTeam) {
        if (newTeam == null || (oldTeam != null && oldTeam != newTeam)) {
            destroy(Messages.TeamCoreBrokenDueToUnclaiming);
        }
        super.onTeamChange(oldTeam, newTeam);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getTeam() != null)
            getTeam().setHasCore(true);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (level != null)
            if (!level.isClientSide)
                if (getTeam() != null)
                    getTeam().setHasCore(false);
    }

    void destroy(Messages message){
        var level = getLevel();
        if (level == null) return;
        if (level.isClientSide) return;
        if (isRemoved()) return;
        if (getTeam() != null) {
            getTeam().setHasCore(false);
            getTeam().sendTeamMessage(message);
        }

        level.destroyBlock(getBlockPos(), false);
    }

    @Override
    protected boolean registerListeners() {
        if (super.registerListeners()) {
            ClaimedChunkEvent.AFTER_UNLOAD.register(unLoadListener);
            return true;
        }
        return false;
    }

    @Override
    protected boolean unRegisterListeners() {
        if (super.unRegisterListeners()) {
            ClaimedChunkEvent.AFTER_UNLOAD.unregister(unLoadListener);
            return true;
        }
        return false;
    }
}
