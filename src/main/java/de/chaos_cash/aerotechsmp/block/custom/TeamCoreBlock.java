package de.chaos_cash.aerotechsmp.block.custom;

import com.mojang.serialization.MapCodec;
import de.chaos_cash.aerotechsmp.block.entity.ModBlockEntities;
import de.chaos_cash.aerotechsmp.block.entity.custom.TeamCoreBlockEntity;
import de.chaos_cash.aerotechsmp.chat.ChatHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!movedByPiston){
            if (level.getBlockEntity(pos) instanceof TeamCoreBlockEntity teamCoreBlockEntity){
                teamCoreBlockEntity.dropItems();
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new TeamCoreBlockEntity(blockPos, blockState);
    }
}
