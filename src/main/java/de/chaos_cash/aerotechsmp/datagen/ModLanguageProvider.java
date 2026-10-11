package de.chaos_cash.aerotechsmp.datagen;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.block.ModBlocks;
import de.chaos_cash.aerotechsmp.block.custom.ArmorBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output) {
        super(output, AerotechSMP.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        for (ArmorBlocks.Color color : ArmorBlocks.Color.values())
        {
            for (ArmorBlocks.Tier tier : ArmorBlocks.Tier.values())
            {
                add(ModBlocks.ARMOR_BLOCKS.get(tier).get(color).get(), ArmorBlocks.getDisplayName(tier, color));
            }
        }
    }
}
