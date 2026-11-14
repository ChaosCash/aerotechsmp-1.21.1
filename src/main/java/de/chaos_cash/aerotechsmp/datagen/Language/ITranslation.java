package de.chaos_cash.aerotechsmp.datagen.Language;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public interface ITranslation {
    String getKey();

    default MutableComponent getTranslation() {
        return Component.translatable(getKey());
    }

    static String getTranslationKey(String name, String type) {
        StringBuilder nameBuilder = new StringBuilder();
        for (char character : name.toCharArray()) {
            if (nameBuilder.isEmpty()) {
                nameBuilder.append(Character.toLowerCase(character));
                continue;
            }

            if (Character.isUpperCase(character))
                nameBuilder.append("_").append(Character.toLowerCase(character));
            else
                nameBuilder.append(character);
        }

        name = nameBuilder.toString();
        return type.toLowerCase() + "." + AerotechSMP.MOD_ID + "." + name;
    }

}
