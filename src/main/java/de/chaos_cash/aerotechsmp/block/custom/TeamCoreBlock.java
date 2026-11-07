package de.chaos_cash.aerotechsmp.block.custom;

import com.mojang.serialization.MapCodec;
import de.chaos_cash.aerotechsmp.block.ModBlocks;
import de.chaos_cash.aerotechsmp.block.entity.custom.TeamCoreBlockEntity;
import de.chaos_cash.aerotechsmp.datagen.Language.Messages;
import de.chaos_cash.aerotechsmp.item.ModItems;
import de.chaos_cash.aerotechsmp.phases.PVPPhase;
import de.chaos_cash.aerotechsmp.phases.PhaseHandler;
import de.chaos_cash.aerotechsmp.team.Team;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;


public class TeamCoreBlock extends BaseEntityBlock {
    public static final MapCodec<TeamCoreBlock> CODEC = simpleCodec(TeamCoreBlock::new);


    public TeamCoreBlock(Properties properties) {
        super(properties);
    }


    @Override
    public boolean onDestroyedByPlayer(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, boolean willHarvest, @NotNull FluidState fluid) {
        boolean dropShard = false;
        if (!level.isClientSide()) {
            var optTeam = Team.get(player);
            if (optTeam.isEmpty())
                return false;
            if (isOwnTeam(level, pos, optTeam.get())) {
                if (!PhaseHandler.getPhase().allowsTeamModifications()) {
                    return false;
                }
            }
            else {
                if (!(PhaseHandler.getPhase() instanceof PVPPhase))
                    return false;
                dropShard = true;
            }
        }

        if (super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid)) {
            if (!level.isClientSide()) {
                if (dropShard)
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), ModItems.MYSTERIOUS_SHARD.toStack(2));
                else
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), ModBlocks.TEAM_CORE.toStack());
            }
            return true;
        }
        return false;
    }


    @Override
    protected void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);

        var ownTeam = TeamCoreBlockEntity.getTeam(pos);
        if (ownTeam != null)
            ownTeam.setHasCore(false);
    }

    public static Team getTeam(@NotNull Level level, @NotNull BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof TeamCoreBlockEntity blockEntity)
            return blockEntity.getTeam();
        else
            return null;
    }


    public boolean isOwnTeam(Level level, BlockPos pos, Team team) {
        if (team == null)
            return false;
        return team == getTeam(level, pos);
    }

    @Override
    protected void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        var team = getTeam(level, pos);
        if (team != null)
            team.setHasCore(true);
    }


    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        var level = context.getLevel();


        if (!PhaseHandler.getPhase().allowsTeamModifications()){
            if (!level.isClientSide())
                Messages.TeamModificationsNotAllowedInPhase.send(context.getPlayer());
            return null;
        }
        if (context.getLevel().dimension() != Level.OVERWORLD) {
            if (level.isClientSide())
                Messages.TeamCoreUnplaceableOutsideOverworld.send(context.getPlayer());
            return null;
        }
        var team = TeamCoreBlockEntity.getTeam(pos);
        if (team == null){
            if (level.isClientSide())
                Messages.TeamCoreNeedsClaimedChunk.send(context.getPlayer());
            return null;
        }
        if (team.hasCore()) {
            if (!level.isClientSide())
                Messages.TeamAlreadyHasCore.send(context.getPlayer());
            return null;
        }
        return this.defaultBlockState();
    }



    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }


    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new TeamCoreBlockEntity(blockPos, blockState);
    }


    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
