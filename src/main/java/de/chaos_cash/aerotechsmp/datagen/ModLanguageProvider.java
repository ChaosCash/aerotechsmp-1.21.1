package de.chaos_cash.aerotechsmp.datagen;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.block.ModBlocks;
import de.chaos_cash.aerotechsmp.block.custom.ArmorBlocks;
import de.chaos_cash.aerotechsmp.item.ModItems;
import de.chaos_cash.aerotechsmp.util.Messages;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output) {
        super(output, AerotechSMP.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(ModBlocks.TEAM_CORE.get(), "Team Core");
        add(ModItems.HEART_CORE.get(), "Heart Core");

        add(ModItems.CLAIM_SHARD.get(), "Claim Shard");
        add(ModItems.CLAIM_CAPSULE.get(), "Claim Capsule");
        add(ModItems.CLAIM_ESSENCE.get(), "Claim Essence");

        add(ModItems.FORCE_LOAD_SHARD.get(), "Force Load Shard");
        add(ModItems.FORCE_LOAD_CAPSULE.get(), "Force Load Capsule");
        add(ModItems.FORCE_LOAD_ESSENCE.get(), "Force Load Essence");

        for (ArmorBlocks.Color color : ArmorBlocks.Color.values()) {
            for (ArmorBlocks.Tier tier : ArmorBlocks.Tier.values()) {
                add(ArmorBlocks.getBlock(tier, color).get(), ArmorBlocks.getDisplayName(tier, color, ArmorBlocks.BlockType.Block));
                add(ArmorBlocks.getStair(tier, color).get(), ArmorBlocks.getDisplayName(tier, color, ArmorBlocks.BlockType.Stairs));
                add(ArmorBlocks.getSlab(tier, color).get(), ArmorBlocks.getDisplayName(tier, color, ArmorBlocks.BlockType.Slab));
            }

        }


        // errors
        add(Messages.ActionRequiresTeam.key, "This action requires a Team");
        add(Messages.ActionRequiresDominationPhase.key, "This action is only allowed in the Domination-Phase");
        add(Messages.TeamCoreNotOwned.key, "This Team Core is not owned by your Team");
        add(Messages.RewardsInaccessibleInPhase.key, "Withdrawing rewards is not allowed in this Phase");
        add(Messages.TeamModificationsNotAllowedInPhase.key, "You cant modify your Team in this Phase");
        add(Messages.TeamCoreForbiddenOutsideOverworld.key, "A Team Core can only be placed in the overworld");
        add(Messages.TeamCoreAlreadyPresent.key, "Your Team already has a Team Core");
        add(Messages.HardMaxClaimLimitReached.key, "Your Team has reached the allowed claim limit");
        add(Messages.HardMaxForceLoadLimitReached.key, "Your Team has reached the allowed force load limit");

        // warnings
        add(Messages.PvpImpeding.key, "PVP-Phase will start shortly");
        add(Messages.OwnTeamCoreDestroyed.key, "Your Team Core has been broken by ");

        // infos
        add(Messages.TeamInvitation.key, "You have been invited to the Team: ");
        add(Messages.AllClaimsDeleted.key, "All claims have been deleted");
        add(Messages.PhaseLengthened.key, "The Phase has been lengthened, because ");
        add(Messages.StartingPhase.key, "Starting ");

        // congratulations
        add(Messages.HeartCoreGrantedRewards.key, "Your Team has been granted Rewards by your Heart Core");
        add(Messages.HeartCoreIncreasedRarity.key, "Your Heart Core has reached a higher rarity");
        add(Messages.HeartCoreCombined.key, "You have picked up a Heart Core, It has combined with yours");
        add(Messages.TeamCoreDestructionRewardsGranted.key, "Your Team has broken an enemy Team Core and received rewards");
        add(Messages.TeamCoreGuardedRewardsGranted.key, "Your Team has successfully protected its Team Core and has received rewards");
        add(Messages.UnclaimedRewardsMessage.key, "Your Team has unclaimed rewards");

        add(Messages.ClaimShardUsed.key, "Your Shard has granted your Team " + ModItems.CLAIM_SHARD.get().claimIncreaseCount + " additional claim");
        add(Messages.ClaimCapsuleUsed.key, "Your Capsule has granted your Team " + ModItems.CLAIM_CAPSULE.get().claimIncreaseCount + " additional claims");
        add(Messages.ClaimEssenceUsed.key, "Your Essence has granted your Team " + ModItems.CLAIM_ESSENCE.get().claimIncreaseCount + " additional claims");

        add(Messages.ForceLoadShardUsed.key, "Your Shard has granted your Team " + ModItems.FORCE_LOAD_SHARD.get().forceLoadIncreaseCount + " additional force load");
        add(Messages.ForceLoadCapsuleUsed.key, "Your Capsule has granted your Team " + ModItems.FORCE_LOAD_CAPSULE.get().forceLoadIncreaseCount + " additional force loads");
        add(Messages.ForceLoadEssenceUsed.key, "Your Essence has granted your Team " + ModItems.FORCE_LOAD_ESSENCE.get().forceLoadIncreaseCount + " additional force loads");
    }

}
