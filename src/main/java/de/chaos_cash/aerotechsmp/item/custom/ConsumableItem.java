package de.chaos_cash.aerotechsmp.item.custom;

import de.chaos_cash.aerotechsmp.util.Messages;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ConsumableItem extends Item {
    protected final Messages successMessage;
    protected final Messages failMessage;
    protected final int useDuration;

    public ConsumableItem(Properties properties, int useDuration, @Nullable Messages successMessage, @Nullable Messages failMessage) {
        super(properties);
        this.successMessage = successMessage;
        this.failMessage = failMessage;
        this.useDuration = useDuration;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        var itemStack = player.getItemInHand(usedHand);

        if (onUse(level, player, itemStack)) {
            if (successMessage != null && !level.isClientSide())
                successMessage.send(player);
            return InteractionResultHolder.consume(itemStack.consumeAndReturn(itemStack.getCount() - 1, player));
        }
        else {
            if (failMessage != null && !level.isClientSide())
                failMessage.send(player);
            return InteractionResultHolder.fail(itemStack);
        }
    }


    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return useDuration;
    }

    protected abstract boolean onUse(@NotNull Level level, @NotNull Player player, @NotNull ItemStack itemStack);
}
