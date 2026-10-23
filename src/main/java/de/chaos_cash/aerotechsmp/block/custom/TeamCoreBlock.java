package de.chaos_cash.aerotechsmp.block.custom;

import com.mojang.serialization.MapCodec;
import de.chaos_cash.aerotechsmp.block.entity.custom.TeamCoreBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TeamCoreBlock extends BaseEntityBlock {
    public static final MapCodec<TeamCoreBlock> CODEC = simpleCodec(TeamCoreBlock::new);

    public TeamCoreBlock(Properties properties) {
        super(properties);
    }



    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack pStack, @NotNull BlockState pState, Level pLevel,
                                                       @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHitResult) {
        if(pLevel.getBlockEntity(pPos) instanceof TeamCoreBlockEntity teamCoreBlockEntity) {
            if(!pPlayer.isCrouching() && !pLevel.isClientSide()) {
                teamCoreBlockEntity.dropRewards(pPlayer);
                return ItemInteractionResult.SUCCESS;
            }
        }

        return ItemInteractionResult.SUCCESS;
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new TeamCoreBlockEntity(blockPos, blockState);
    }



    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if (level.isClientSide)
            return;

       if (placer instanceof Player player){
           if (level.getBlockEntity(pos) instanceof  TeamCoreBlockEntity blockEntity)
               blockEntity.setPlacer(player);
       }
    }


    @Override
    protected void onRemove(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean movedByPiston) {
        if (!level.isClientSide()) {
            if (level.getBlockEntity(pos) instanceof TeamCoreBlockEntity coreBE) {
                coreBE.onRemove();
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public boolean onDestroyedByPlayer(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, boolean willHarvest, @NotNull FluidState fluid) {
        if(!level.isClientSide()){
            var blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof TeamCoreBlockEntity coreBlockEntity){
                var team = coreBlockEntity.getTeam();
                if (team != null)
                    team.onCoreBrokeByPlayer(player);
            }
        }

        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }
}
