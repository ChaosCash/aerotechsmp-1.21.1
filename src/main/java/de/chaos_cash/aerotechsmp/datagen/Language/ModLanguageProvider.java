package de.chaos_cash.aerotechsmp.datagen.Language;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.block.ModBlocks;
import de.chaos_cash.aerotechsmp.block.custom.ArmorBlocks;
import de.chaos_cash.aerotechsmp.item.ModItems;
import de.chaos_cash.aerotechsmp.item.custom.ModItem;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output) {
        super(output, AerotechSMP.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addNames();
        addMessages();
        addTooltips();
    }


    protected void addNames() {
        add(ModItems.MYSTERIOUS_SHARD.get(), "Mysterious Shard");
        add(ModBlocks.TEAM_CORE.get(), "Team Core");

        add(ModItems.CLAIM_SHARD.get(), "Claim Shard");
        add(ModItems.CLAIM_CAPSULE.get(), "Claim Capsule");
        add(ModItems.CLAIM_ESSENCE.get(), "Claim Essence");

        add(ModItems.FORCE_LOAD_SHARD.get(), "Force Load Shard");
        add(ModItems.FORCE_LOAD_CAPSULE.get(), "Force Load Capsule");
        add(ModItems.FORCE_LOAD_ESSENCE.get(), "Force Load Essence");

        for (ArmorBlocks.Color color : ArmorBlocks.Color.values())
            for (ArmorBlocks.Tier tier : ArmorBlocks.Tier.values()) {
                add(ArmorBlocks.getBlock(tier, color).get(), ArmorBlocks.getDisplayName(tier, color, ArmorBlocks.BlockType.Block));
                add(ArmorBlocks.getStair(tier, color).get(), ArmorBlocks.getDisplayName(tier, color, ArmorBlocks.BlockType.Stairs));
                add(ArmorBlocks.getSlab(tier, color).get(), ArmorBlocks.getDisplayName(tier, color, ArmorBlocks.BlockType.Slab));
            }
    }

    protected void addMessages() {
        // errors
        add(Messages.ActionRequiresTeam.key, "This action requires a Team");
        add(Messages.ActionRequiresPvpPhase.key, "This action is only allowed in the PVP-Phase");
        add(Messages.TeamModificationsNotAllowedInPhase.key, "You cant modify your Team in this Phase");
        add(Messages.TeamAlreadyFull.key, "This team is already full");
        add(Messages.ClaimGrantingLimitReached.key, "This item can't grant any more claims");
        add(Messages.ForceLoadGrantingLimitReached.key, "This item can't grant any more force loads");
        add(Messages.TeamCoreNeedsClaimedChunk.key, "Team Cores can only be placed in claimed chunks");
        add(Messages.TeamAlreadyHasCore.key, "This Team already has a core");
        add(Messages.TeamCoreUnplaceableOutsideOverworld.key, "Team Cores are only placeable in the overworld");

        // warnings
        add(Messages.PvpImpeding.key, "PVP-Phase will start shortly");
        add(Messages.OwnTeamCoreDestroyed.key, "Your Team Core has been Destroyed");

        // infos
        add(Messages.PhaseLengthened.key, "The Phase has been lengthened, because ");
        add(Messages.StartingPhase.key, "Starting ");
        add(Messages.GameIsPaused.key, "Game is Paused");

        // rewards
        add(Messages.ClaimShardUsed.key, "+" + ModItems.CLAIM_SHARD.get().claimIncreaseCount + " claim");
        add(Messages.ClaimCapsuleUsed.key, "+" + ModItems.CLAIM_CAPSULE.get().claimIncreaseCount + " claims");
        add(Messages.ClaimEssenceUsed.key, "+" + ModItems.CLAIM_ESSENCE.get().claimIncreaseCount + " claims");
        add(Messages.ForceLoadShardUsed.key, "+" + ModItems.FORCE_LOAD_SHARD.get().forceLoadIncreaseCount + " force load");
        add(Messages.ForceLoadCapsuleUsed.key, "+" + ModItems.FORCE_LOAD_CAPSULE.get().forceLoadIncreaseCount + " force load");
        add(Messages.ForceLoadEssenceUsed.key, "+" + ModItems.FORCE_LOAD_ESSENCE.get().forceLoadIncreaseCount + " force load");
        add(Messages.EnemyTeamCoreDestroyed.key, "Your Team has broken an enemy Team Core and will receive rewards in the next Peaceful Phase");
        add(Messages.TeamCoreSurvived.key, "Your Team has survived the PvP-Phase. Your Team has received Rewards");
    }


    protected void addTooltips() {
        addTooltip(ModItems.MYSTERIOUS_SHARD.get(), "Received as a reward from breaking enemy Team Cores or protecting your own in the PVP-Phase");

        var claimTooltip = "Grants +%s claim%s (max %s)";
        var claimShard = ModItems.CLAIM_SHARD.get();
        var claimCapsule = ModItems.CLAIM_CAPSULE.get();
        var claimEssence = ModItems.CLAIM_ESSENCE.get();
        addTooltip(claimShard, claimTooltip.formatted(claimShard.claimIncreaseCount, claimShard.claimIncreaseCount == 1 ? "" : "s", claimShard.claimLimit));
        addTooltip(claimCapsule, claimTooltip.formatted(claimCapsule.claimIncreaseCount, claimCapsule.claimIncreaseCount == 1 ? "" : "s", claimCapsule.claimLimit));
        addTooltip(claimEssence, claimTooltip.formatted(claimEssence.claimIncreaseCount, claimEssence.claimIncreaseCount == 1 ? "" : "s", claimEssence.claimLimit));

        var forceLoadTooltip = "Grants +%s force load%s (max %s)";
        var forceLoadShard = ModItems.FORCE_LOAD_SHARD.get();
        var forceLoadCapsule = ModItems.FORCE_LOAD_CAPSULE.get();
        var forceLoadEssence = ModItems.FORCE_LOAD_ESSENCE.get();
        addTooltip(forceLoadShard, forceLoadTooltip.formatted(forceLoadShard.forceLoadIncreaseCount, forceLoadShard.forceLoadIncreaseCount == 1 ? "" : "s", forceLoadShard.forceLoadLimit));
        addTooltip(forceLoadCapsule, forceLoadTooltip.formatted(forceLoadCapsule.forceLoadIncreaseCount, forceLoadCapsule.forceLoadIncreaseCount == 1 ? "" : "s", forceLoadCapsule.forceLoadLimit));
        addTooltip(forceLoadEssence, forceLoadTooltip.formatted(forceLoadEssence.forceLoadIncreaseCount, forceLoadEssence.forceLoadIncreaseCount == 1 ? "" : "s", forceLoadEssence.forceLoadLimit));

    }



    void addTooltip(ModItem<?> item, String translation) {
        add(getTooltipKey(item), translation);
    }


    public static String getTooltipKey(ModItem<?> item) {
        return "tooltip." + AerotechSMP.MOD_ID + "." + item.getId();
    }
}
