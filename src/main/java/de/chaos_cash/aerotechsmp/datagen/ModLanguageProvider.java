package de.chaos_cash.aerotechsmp.datagen;

import de.chaos_cash.aerotechsmp.AerotechSMP;
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
        add(Messages.ClaimGrantingLimitReached.key, "This item can't grant any more claims");
        add(Messages.ForceLoadGrantingLimitReached.key, "This item can't grant any more force loads");

        // warnings
        add(Messages.PvpImpeding.key, "PVP-Phase will start shortly");

        // infos
        add(Messages.PhaseLengthened.key, "The Phase has been lengthened, because ");
        add(Messages.StartingPhase.key, "Starting ");

        add(Messages.ClaimCapsuleUsed.key, "Your Capsule has granted your Team " + ModItems.CLAIM_CAPSULE.get().claimIncreaseCount + " additional claims");
        add(Messages.ForceLoadCapsuleUsed.key, "Your Capsule has granted your Team " + ModItems.FORCE_LOAD_CAPSULE.get().forceLoadIncreaseCount + " additional force loads");
    }

}
