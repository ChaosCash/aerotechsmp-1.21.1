package de.chaos_cash.aerotechsmp.util;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import dev.ryanhcode.sable.Sable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks{
        public static final TagKey<Block> SUPER_HEAVY
                = BlockTags.create(ResourceLocation.fromNamespaceAndPath(Sable.MOD_ID, "super_heavy"));
        public static final TagKey<Block> HEAVY
                = BlockTags.create(ResourceLocation.fromNamespaceAndPath(Sable.MOD_ID, "heavy"));
        public static final TagKey<Block> LIGHT
                = BlockTags.create(ResourceLocation.fromNamespaceAndPath(Sable.MOD_ID, "light"));

        private static TagKey<Block> createTag(String name)
        {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> ARMOR_BASIC = createTag("armor_basic");
        public static final TagKey<Item> ARMOR_HARDENED = createTag("armor_hardened");
        public static final TagKey<Item> ARMOR_COMPRESSED = createTag("armor_compressed");

        private static TagKey<Item> createTag(String name)
        {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID, name));
        }
    }
}
