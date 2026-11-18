package de.chaos_cash.aerotechsmp;

import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();


    public static final ModConfigSpec.IntValue MAX_TEAM_SIZE = BUILDER
            .defineInRange("maxTeamSize", 2, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue BORDER_DIAMETER = BUILDER
            .defineInRange("borderDiameter", 4000, 1000, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue SHARD_REWARD_COUNT = BUILDER
            .comment("The amount of shards received per reward")
            .defineInRange("shardRewardCount", 3, 1, 10);


    public static final ModConfigSpec SPEC = BUILDER.build();
}
