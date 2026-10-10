package de.chaos_cash.aerotechsmp.datagen;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.block.ModBlocks;
import de.chaos_cash.aerotechsmp.block.custom.ArmorBlock;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output) {
        super(output, AerotechSMP.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        for (ArmorBlock.Color color : ArmorBlock.Color.values())
        {
            for (ArmorBlock.Tier tier : ArmorBlock.Tier.values())
            {
                add(ModBlocks.ARMOR_BLOCKS.get(tier).get(color).get(), ArmorBlock.getDisplayName(tier, color));
            }
        }
    }
}
