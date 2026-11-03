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

        public static final TagKey<Block> HALF_VOLUME
                = BlockTags.create(ResourceLocation.fromNamespaceAndPath(Sable.MOD_ID, "half_volume"));

        private static TagKey<Block> createTag(String name)
        {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID, name));
        }
    }

    public static class Items {
        public static class Armor {
            public static class Block {
                public static final TagKey<Item> LIGHT = createTag("armor/basic/block");
                public static final TagKey<Item> HARDENED = createTag("armor/hardened/block");
                public static final TagKey<Item> COMPRESSED = createTag("armor/compressed/block");
            }

            public static class Stair {
                public static final TagKey<Item> LIGHT = createTag("armor/basic/stair");
                public static final TagKey<Item> HARDENED = createTag("armor/hardened/stair");
                public static final TagKey<Item> COMPRESSED = createTag("armor/compressed/stair");
            }

            public static class Slab {
                public static final TagKey<Item> LIGHT = createTag("armor/basic/slab");
                public static final TagKey<Item> HARDENED = createTag("armor/hardened/slab");
                public static final TagKey<Item> COMPRESSED = createTag("armor/compressed/slab");
            }
        }



        private static TagKey<Item> createTag(String name)
        {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID, name));
        }
    }
}
