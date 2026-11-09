package de.chaos_cash.aerotechsmp.block.custom;

import com.mojang.serialization.MapCodec;
import de.chaos_cash.aerotechsmp.block.ModBlocks;
import de.chaos_cash.aerotechsmp.block.entity.custom.TeamCoreBlockEntity;
import de.chaos_cash.aerotechsmp.datagen.Language.Messages;
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
        boolean destroyedByEnemy = false;
        var optTeam = Team.get(player);
        if (optTeam.isEmpty()) {
            if (level.isClientSide)
                Messages.ActionRequiresTeam.notify(player);
            return false;
        }

        if (isOwnTeam(level, pos, optTeam.get())) {
            if (!PhaseHandler.getPhase().allowsTeamModifications()) {
                if (!level.isClientSide)
                    Messages.TeamModificationsNotAllowedInPhase.notify(player);
                return false;
            }
        }
        else {
            if (!(PhaseHandler.getPhase() instanceof PVPPhase)) {
                if (!level.isClientSide)
                    Messages.ActionRequiresPvpPhase.notify(player);
                return false;
            }
            destroyedByEnemy = true;
        }

        if (super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid)) {
            if (!level.isClientSide()) {
                if (destroyedByEnemy && PhaseHandler.getPhase() instanceof PVPPhase pvpPhase) {
                    var coreTeam = getTeam(level, pos);
                    if (coreTeam != null)
                        pvpPhase.onTeamCoreDestruction(coreTeam, optTeam.get());}
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
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        var level = context.getLevel();


        if (!PhaseHandler.getPhase().allowsTeamModifications()){
            if (!level.isClientSide())
                Messages.TeamModificationsNotAllowedInPhase.notify(context.getPlayer());
            return null;
        }
        if (context.getLevel().dimension() != Level.OVERWORLD) {
            if (level.isClientSide())
                Messages.TeamCoreUnplaceableOutsideOverworld.notify(context.getPlayer());
            return null;
        }
        var team = TeamCoreBlockEntity.getTeam(pos);
        if (team == null){
            if (level.isClientSide())
                Messages.TeamCoreNeedsClaimedChunk.notify(context.getPlayer());
            return null;
        }
        if (team.hasCore()) {
            if (!level.isClientSide())
                Messages.TeamAlreadyHasCore.notify(context.getPlayer());
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
