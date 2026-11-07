package de.chaos_cash.aerotechsmp.datagen.Language;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public interface ITranslation {
    String getKey();

    default MutableComponent getTranslation() {
        return Component.translatable(getKey());
    }
}
