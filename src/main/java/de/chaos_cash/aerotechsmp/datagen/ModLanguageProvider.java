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
        add(Messages.ClaimLimitExceeded.key, "Claim Limit Exceeded");
        add(Messages.ClaimLimitExceededLong.key, "Your Team has already reached its Claim Limit");
        add(Messages.ForceLoadLimitExceeded.key, "Force Load Limit Exceeded");
        add(Messages.ForceLoadLimitExceededLong.key, "Your Team has already reached its Force Load Limit");
        add(Messages.TeamCoreNotOwned.key, "This Team Core is not owned by your Team");
        add(Messages.RewardsInaccessibleInPhase.key, "Withdrawing rewards is not allowed in this Phase");
        add(Messages.TeamModificationsNotAllowedInPhase.key, "You cant modify your Team in this Phase");
        add(Messages.TeamCoreForbiddenOutsideOverworld.key, "A Team Core can only be placed in the overworld");
        add(Messages.TeamCoreAlreadyPresent.key, "Your Team already has a Team Core");

        // warnings
        add(Messages.PVPImpeding.key, "PVP-Phase will start shortly");
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
    }

}
