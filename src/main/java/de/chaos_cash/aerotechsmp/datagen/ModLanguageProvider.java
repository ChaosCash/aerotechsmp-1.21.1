package de.chaos_cash.aerotechsmp.datagen;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.block.custom.ArmorBlocks;
import de.chaos_cash.aerotechsmp.item.ModItems;
import de.chaos_cash.aerotechsmp.item.custom.ClaimGrantingItem;
import de.chaos_cash.aerotechsmp.item.custom.ForceLoadGrantingItem;
import de.chaos_cash.aerotechsmp.util.Messages;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredItem;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output) {
        super(output, AerotechSMP.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addClaimGrantingTooltip(ModItems.CLAIM_SHARD);
        addClaimGrantingTooltip(ModItems.CLAIM_CAPSULE);
        addClaimGrantingTooltip(ModItems.CLAIM_ESSENCE);

        addForceLoadGrantingTooltip(ModItems.FORCE_LOAD_SHARD);
        addForceLoadGrantingTooltip(ModItems.FORCE_LOAD_CAPSULE);
        addForceLoadGrantingTooltip(ModItems.FORCE_LOAD_ESSENCE);


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


        // errors
        add(Messages.ActionRequiresTeam.key, "This action requires a Team");
        add(Messages.TeamModificationsNotAllowedInPhase.key, "You cant modify your Team in this Phase");
        add(Messages.TeamAlreadyFull.key, "This team is already full");
        add(Messages.ClaimShardUsed.key, "+" + ModItems.CLAIM_SHARD.get().claimIncreaseCount + " claim");
        add(Messages.ClaimCapsuleUsed.key, "+" + ModItems.CLAIM_CAPSULE.get().claimIncreaseCount + " claims");
        add(Messages.ClaimEssenceUsed.key, "+" + ModItems.CLAIM_ESSENCE.get().claimIncreaseCount + " claims");
        add(Messages.ForceLoadShardUsed.key, "+" + ModItems.FORCE_LOAD_SHARD.get().forceLoadIncreaseCount + " force load");
        add(Messages.ForceLoadCapsuleUsed.key, "+" + ModItems.FORCE_LOAD_CAPSULE.get().forceLoadIncreaseCount + " force load");
        add(Messages.ForceLoadEssenceUsed.key, "+" + ModItems.FORCE_LOAD_ESSENCE.get().forceLoadIncreaseCount + " force load");
        add(Messages.ClaimGrantingLimitReached.key, "This item can't grant any more claims");
        add(Messages.ForceLoadGrantingLimitReached.key, "This item can't grant any more force loads");

        // warnings
        add(Messages.PvpImpeding.key, "PVP-Phase will start shortly");

        // infos
        add(Messages.PhaseLengthened.key, "The Phase has been lengthened, because ");
        add(Messages.StartingPhase.key, "Starting ");
    }


    void addClaimGrantingTooltip(DeferredItem<ClaimGrantingItem> item) {
        add(getTooltipKey(item), "This item increases the claim limit of your team by +" + item.get().claimIncreaseCount + " (max " + item.get().claimLimit + ")");
    }

    void addForceLoadGrantingTooltip(DeferredItem<ForceLoadGrantingItem> item) {
        add(getTooltipKey(item), "This item increases the force load limit of your team by +" + item.get().forceLoadIncreaseCount + " (max " + item.get().forceLoadLimit + ")");
    }

    public static String getTooltipKey(DeferredItem<? extends Item> item) {
        return getTooltipKey(item.getId().getPath());
    }

    public static String getTooltipKey(String itemId) {
        return "tooltip." + AerotechSMP.MOD_ID + "." + itemId;
    }
}
