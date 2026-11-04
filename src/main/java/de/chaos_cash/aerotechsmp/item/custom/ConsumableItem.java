package de.chaos_cash.aerotechsmp.item.custom;

import de.chaos_cash.aerotechsmp.util.Messages;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class ConsumableItem extends Item {
    protected final Messages successMessage;
    protected final Messages failMessage;
    protected final Component toolTip;
    protected final int useDuration;

    public ConsumableItem(Properties properties, int useDuration, @Nullable Messages successMessage, @Nullable Messages failMessage, Component toolTip) {
        super(properties);
        this.successMessage = successMessage;
        this.failMessage = failMessage;
        this.useDuration = useDuration;
        this.toolTip = toolTip;
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

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(toolTip);
    }

    protected abstract boolean onUse(@NotNull Level level, @NotNull Player player, @NotNull ItemStack itemStack);
}
