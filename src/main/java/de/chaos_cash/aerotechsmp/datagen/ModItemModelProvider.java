package de.chaos_cash.aerotechsmp.datagen;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.item.ModItems;
import net.minecraft.data.PackOutput;

import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;


public class ModItemModelProvider extends ItemModelProvider {


    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, AerotechSMP.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.CLAIM_SHARD.get());
        basicItem(ModItems.CLAIM_CAPSULE.get());
        basicItem(ModItems.CLAIM_ESSENCE.get());

        basicItem(ModItems.FORCE_LOAD_SHARD.get());
        basicItem(ModItems.FORCE_LOAD_CAPSULE.get());
        basicItem(ModItems.FORCE_LOAD_ESSENCE.get());
    }

}
