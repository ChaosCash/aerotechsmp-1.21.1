package de.chaos_cash.aerotechsmp.datagen.Language;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.util.ChatHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;


public enum Messages implements ITranslation{
    ActionRequiresTeam(Types.Error),
    ActionRequiresPvpPhase(Types.Error),
    TeamModificationsNotAllowedInPhase(Types.Error),
    TeamAlreadyFull(Types.Error),
    ClaimGrantingLimitReached(Types.Error),
    ForceLoadGrantingLimitReached(Types.Error),
    TeamCoreUnplaceableOutsideOverworld(Types.Error),
    TeamAlreadyHasCore(Types.Error),
    TeamCoreNeedsClaimedChunk(Types.Error),

    PvpImpeding(Types.Warning),
    OwnTeamCoreDestroyed(Types.Warning),

    PhaseLengthened(Types.Info),
    StartingPhase(Types.Info),
    TeamCoreBrokenDueToUnclaiming(Types.Info),

    ClaimShardUsed(Types.Congratulation),
    ClaimCapsuleUsed(Types.Congratulation),
    ClaimEssenceUsed(Types.Congratulation),
    ForceLoadShardUsed(Types.Congratulation),
    ForceLoadCapsuleUsed(Types.Congratulation),
    ForceLoadEssenceUsed(Types.Congratulation),
    EnemyTeamCoreDestroyed(Types.Congratulation),
    TeamCoreSurvived(Types.Congratulation),

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


    @Override
    public String getKey() {
        return key;
    }

    @Override
    public MutableComponent getTranslation() {
        return ITranslation.super.getTranslation().withStyle(type.formatting);
    }

    public void broadcast() {
        ChatHandler.broadcast(getTranslation());
    }


    public void send(Player player) {
        ChatHandler.sendMessage(player, getTranslation());
    }

    public void send(ServerPlayer player) {
        ChatHandler.sendMessage(player, getTranslation());
    }


    public void notify(Player player) {
        ChatHandler.notify(player, getTranslation());
    }

    @SuppressWarnings("unused")
    public void notify(ServerPlayer player) {
        ChatHandler.notify(player, getTranslation());
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