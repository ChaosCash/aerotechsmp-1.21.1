package de.chaos_cash.aerotechsmp.item.custom;

import de.chaos_cash.aerotechsmp.datagen.Language.ModLanguageProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ModItem<T extends ModItem<?>> extends Item {
    boolean hasTooltip = false;

    public ModItem(Properties properties) {
        super(properties);
    }


    public String getId() {
        return BuiltInRegistries.ITEM.wrapAsHolder(this).getRegisteredName();
    }


    @SuppressWarnings("unchecked")
    public T withTooltip() {
        hasTooltip = true;
        return (T) this;
    }


    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(getTooltip(stack));
    }

    protected Component getTooltip(@NotNull ItemStack stack) {
        return Component.translatable(ModLanguageProvider.getTooltipKey(this));
    }
}
