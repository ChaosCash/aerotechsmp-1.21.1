package de.chaos_cash.aerotechsmp.block.custom;

import com.mojang.serialization.MapCodec;
import de.chaos_cash.aerotechsmp.block.entity.custom.ShipProtectorBlockEntity;
import de.chaos_cash.aerotechsmp.datagen.Language.MenuNames;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class ShipProtectorBlock extends BaseEntityBlock {
    public static final MapCodec<ShipProtectorBlock> CODEC = simpleCodec(ShipProtectorBlock::new);



    public ShipProtectorBlock(Properties properties) {
        super(properties);
    }


    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, Level level, @NotNull BlockPos pos,
                                                       @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if(level.getBlockEntity(pos) instanceof ShipProtectorBlockEntity shipProtectorBlockEntity) {
            if(!level.isClientSide()) {
                player.openMenu(new SimpleMenuProvider(shipProtectorBlockEntity, MenuNames.ShipProtector.getTranslation()), pos);
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
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new ShipProtectorBlockEntity(blockPos, blockState);
    }


    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
