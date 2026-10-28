package de.chaos_cash.aerotechsmp.util;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;


public enum Messages {
    ActionRequiresTeam(Types.Error),
    ActionRequiresDominationPhase(Types.Error),
    TeamCoreNotOwned(Types.Error),
    RewardsInaccessibleInPhase(Types.Error),
    TeamModificationsNotAllowedInPhase(Types.Error),
    TeamCoreForbiddenOutsideOverworld(Types.Error),
    TeamCoreAlreadyPresent(Types.Error),
    HardMaxClaimLimitReached(Types.Error),
    HardMaxForceLoadLimitReached(Types.Error),

    PvpImpeding(Types.Warning),
    OwnTeamCoreDestroyed(Types.Warning),

    TeamInvitation(Types.Info),
    AllClaimsDeleted(Types.Info),
    PhaseLengthened(Types.Info),
    StartingPhase(Types.Info),

    HeartCoreGrantedRewards(Types.Congratulation),
    HeartCoreIncreasedRarity(Types.Congratulation),
    HeartCoreCombined(Types.Congratulation),
    TeamCoreDestructionRewardsGranted(Types.Congratulation),
    TeamCoreGuardedRewardsGranted(Types.Congratulation),
    UnclaimedRewardsMessage(Types.Congratulation),

    ClaimShardUsed(Types.Congratulation),
    ClaimCapsuleUsed(Types.Congratulation),
    ClaimEssenceUsed(Types.Congratulation),

    ForceLoadShardUsed(Types.Congratulation),
    ForceLoadCapsuleUsed(Types.Congratulation),
    ForceLoadEssenceUsed(Types.Congratulation),
    ;



    public final String key;
    public final Types type;

    Messages(Types type){
        key = getMessageTranslationKey(type);
        this.type = type;
    }


    String getMessageTranslationKey(Types type) {
        StringBuilder nameBuilder = new StringBuilder();
        for (char character : this.name().toCharArray()) {
            if (nameBuilder.isEmpty()) {
                nameBuilder.append(Character.toLowerCase(character));
                continue;
            }

            if (Character.isUpperCase(character))
                nameBuilder.append("_").append(Character.toLowerCase(character));
            else
                nameBuilder.append(character);
        }

        String name = nameBuilder.toString();


        return "message." + type.name().toLowerCase() + "." + AerotechSMP.MOD_ID + "." + name;
    }


    public MutableComponent getTranslation() {
        return Component.translatable(key).withStyle(type.formatting);
    }


    public void broadcast() {
        ChatHandler.broadcast(getTranslation());
    }

    public void send(UUID playerId) {
        ChatHandler.sendMessage(playerId, getTranslation());
    }

    public void send(Player player) {
        ChatHandler.sendMessage(player, getTranslation());
    }

    public void send(ServerPlayer player) {
        ChatHandler.sendMessage(player, getTranslation());
    }



    public enum Types {
        Info(ChatFormatting.BLUE),
        Congratulation(ChatFormatting.GOLD),
        Success(ChatFormatting.GREEN),
        Warning(ChatFormatting.DARK_PURPLE),
        Error(ChatFormatting.RED);

        public final ChatFormatting formatting;

        Types(ChatFormatting formatting) {
            this.formatting = formatting;
        }
    }
}