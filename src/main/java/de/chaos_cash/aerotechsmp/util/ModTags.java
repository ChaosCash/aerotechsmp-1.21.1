package de.chaos_cash.aerotechsmp.util;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks{

        private static TagKey<Block> createTag(String name)
        {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(AerotechSMP.MODID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> ARMOR_BASIC = createTag("armor_basic");
        public static final TagKey<Item> ARMOR_HARDENED = createTag("armor_hardened");
        public static final TagKey<Item> ARMOR_COMPRESSED = createTag("armor_compressed");

        private static TagKey<Item> createTag(String name)
        {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(AerotechSMP.MODID, name));
        }
    }
}
