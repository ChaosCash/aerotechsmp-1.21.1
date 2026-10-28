package de.chaos_cash.aerotechsmp.team;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.block.entity.custom.TeamCoreBlockEntity;
import de.chaos_cash.aerotechsmp.phases.PhaseHandler;
import de.chaos_cash.aerotechsmp.util.ChatHandler;
import de.chaos_cash.aerotechsmp.util.Messages;
import dev.ftb.mods.ftbchunks.FTBChunksAPIImpl;
import dev.ftb.mods.ftbchunks.FTBChunksWorldConfig;
import dev.ftb.mods.ftbchunks.data.ChunkTeamDataImpl;
import dev.ftb.mods.ftbchunks.data.PartyLimitMode;
import dev.ftb.mods.ftbteams.FTBTeamsAPIImpl;
import dev.ftb.mods.ftbteams.api.TeamRank;
import dev.ftb.mods.ftbteams.api.property.BooleanProperty;
import dev.ftb.mods.ftbteams.api.property.IntProperty;
import dev.ftb.mods.ftbteams.api.property.StringListProperty;
import dev.ftb.mods.ftbteams.data.PartyTeam;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.List;



@EventBusSubscriber(modid = AerotechSMP.MOD_ID)
public class Team {
    public static final int maxSize = 2;
    public static final int defaultMaxClaims = 9;
    public static final int defaultMaxForceLoads = 0;
    public static final int hardMaxClaims = 500;
    public static final int hardMaxForceLoads = 16;
    private static final Map<UUID, Team> teams = new HashMap<>();
    private static final Map<UUID, Team> invites = new HashMap<>();


    private static final BooleanProperty hasCorePropertyType = new BooleanProperty(ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID, "has_core"), false);
    private static final IntProperty corePosXPropertyType = new IntProperty(ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID, "core_pos_x"), null);
    private static final IntProperty corePosYPropertyType = new IntProperty(ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID, "core_pos_y"), null);
    private static final IntProperty corePosZPropertyType = new IntProperty(ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID, "core_pos_z"), null);
    private static final StringListProperty rewardsPropertyType = new StringListProperty(ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID, "rewards"), () -> null);


    @NotNull private final PartyTeam ftbTeam;
    private final List<Reward> rewards = new ArrayList<>();
    private BlockPos corePos = null;
    private final ChunkTeamDataImpl chunkTeamData;
    private final ChunkTeamDataImpl ownerData = FTBChunksAPIImpl.INSTANCE.getManager().getPersonalData(getOwner());




    //<editor-fold desc="Creating / getting / loading">

    private Team(PartyTeam ftbTeam){
        this.ftbTeam = ftbTeam;

        teams.put(ftbTeam.getTeamId(), this);

        loadRewards();
        loadCore();

        chunkTeamData = FTBChunksAPIImpl.INSTANCE.getManager().getOrCreateData(ftbTeam);
    }


    private void loadCore() {
        boolean hasCore = ftbTeam.getProperty(hasCorePropertyType);
        if (!hasCore) return;

        var position = new BlockPos(
            ftbTeam.getProperty(corePosXPropertyType),
            ftbTeam.getProperty(corePosYPropertyType),
            ftbTeam.getProperty(corePosZPropertyType)
        );

        if (AerotechSMP.getServer().overworld().getBlockEntity(position) instanceof TeamCoreBlockEntity) {
            corePos = position;
        }
    }

    private void loadRewards(){
        var rewards = ftbTeam.getProperty(rewardsPropertyType);
        if (rewards == null) {
            return;
        }

        for (var reward : rewards){
            try {
                addReward(Reward.getInstance(TagParser.parseTag(reward)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static Optional<Team> get(ServerPlayer player){
        var ftbTeam = FTBTeamsAPIImpl.INSTANCE.getManager().getTeamForPlayerID(player.getUUID());
        return ftbTeam.flatMap(team -> get(team.getTeamId()));
    }

    public static Optional<Team> get(Player player){
        var ftbTeam = FTBTeamsAPIImpl.INSTANCE.getManager().getTeamForPlayerID(player.getUUID());
        return ftbTeam.flatMap(team -> get(team.getTeamId()));
    }

    public static Optional<Team> get(UUID teamID){
        if (teams.containsKey(teamID))
            return Optional.ofNullable(teams.get(teamID));

        var optTeam = FTBTeamsAPIImpl.INSTANCE.getManager().getTeamByID(teamID);

        if (optTeam.isPresent() && optTeam.get() instanceof PartyTeam party)
            return Optional.of(new Team(party));
        else
            return Optional.empty();
    }

    public static List<Team> list() {
        var teams = new ArrayList<Team>();

        for (var ftbTeam : FTBTeamsAPIImpl.INSTANCE.getManager().getTeams())
            get(ftbTeam.getTeamId()).ifPresent(teams::add);

        return teams;
    }



    public static void create(ServerPlayer player, String name) throws CommandSyntaxException {
        PartyTeam ftbTeam = FTBTeamsAPIImpl.INSTANCE.getManager().createParty(player, name);
        new Team(ftbTeam);
        ChatHandler.updateCommands(player);
    }

    //</editor-fold>



    //<editor-fold desc="API">

    public UUID getId() {return ftbTeam.getId();}

    public UUID getOwner() {
        return ftbTeam.getOwner();
    }

    public String getDisplayName() {
        return ftbTeam.getDisplayName();
    }

    public int getTeamSize() {return ftbTeam.getPlayersByRank(TeamRank.ALLY).size();}

    public int getMaxTeamSize() {return maxSize;}

    public List<ServerPlayer> getOnlinePlayers(){
        return ftbTeam.getOnlineMembers();
    }

    public int getMaxClaims() {return chunkTeamData.getMaxClaimChunks();}

    public int getMaxForceLoads() {return chunkTeamData.getMaxForceLoadChunks();}

    public int getClaimCount() {
        return chunkTeamData.getClaimedChunks().size();
    }

    public int getForceLoadCount() {
        return chunkTeamData.getForceLoadedChunks().size();
    }


    public boolean isFull() {return getTeamSize() >= maxSize;}

    public boolean isOwner(ServerPlayer player){
        return ftbTeam.isOwner(player.getUUID());
    }

    public boolean setCore(TeamCoreBlockEntity teamCoreBlockEntity) {
        if (hasCore()) return false;

        corePos = teamCoreBlockEntity.getBlockPos();
        ftbTeam.setProperty(hasCorePropertyType, true);
        ftbTeam.setProperty(corePosXPropertyType, corePos.getX());
        ftbTeam.setProperty(corePosYPropertyType, corePos.getY());
        ftbTeam.setProperty(corePosZPropertyType, corePos.getZ());
        return true;
    }

    public void removeCore() {
        if (corePos != null)
            if (AerotechSMP.getServer().overworld().getBlockEntity(corePos) instanceof TeamCoreBlockEntity)
                AerotechSMP.getServer().overworld().destroyBlock(corePos, true);

        corePos = null;
        ftbTeam.setProperty(hasCorePropertyType, false);
        ftbTeam.setProperty(corePosXPropertyType, 0);
        ftbTeam.setProperty(corePosYPropertyType, 0);
        ftbTeam.setProperty(corePosZPropertyType, 0);
    }

    public boolean hasCore() {
        if (corePos == null) return false;
        if (!(AerotechSMP.getServer().overworld().getBlockEntity(corePos) instanceof TeamCoreBlockEntity)){
            removeCore();
            return false;
        }

        return true;
    }

    public void onCoreBrokeByPlayer(Player player) {
        if (!ftbTeam.isMember(player.getUUID()) || PhaseHandler.getPhase().allowsTeamModifications())
            sendTeamMessage(Messages.OwnTeamCoreDestroyed.getTranslation().append(player.getDisplayName().getString()));

        RewardHandler.onTeamCoreBreak(player, this);
    }


    public void increaseMaxClaims(int increase) {
        ownerData.setExtraClaimChunks(ownerData.getExtraClaimChunks() + increase);
        ownerData.updateLimits();
        ownerData.saveNow();

        chunkTeamData.updateLimits();
        chunkTeamData.saveNow();
    }

    public void increaseMaxForceLoads(int increase) {
        ownerData.setExtraForceLoadChunks(ownerData.getExtraForceLoadChunks() + increase);
        ownerData.saveNow();
        ownerData.updateLimits();

        var teamData = FTBChunksAPIImpl.INSTANCE.getManager().getOrCreateData(ftbTeam);
        teamData.updateLimits();
        teamData.saveNow();
    }


    public boolean hasRewards() {return !rewards.isEmpty();}

    public void addReward(Reward reward){
        if (AerotechSMP.getServer().overworld().isClientSide()) {
            return;
        }

        rewards.add(reward);

        List<String> rewardsProperty = ftbTeam.getProperty(rewardsPropertyType);
        if (rewardsProperty == null)
            rewardsProperty = new ArrayList<>();
        rewardsProperty.add(reward.save().toString());
        ftbTeam.setProperty(rewardsPropertyType, rewardsProperty);
    }

    public void removeReward(Reward reward){
        int index = rewards.indexOf(reward);
        rewards.remove(index);

        var rewardsProperty = ftbTeam.getProperty(rewardsPropertyType);
        rewardsProperty.remove(index);
        ftbTeam.setProperty(rewardsPropertyType, rewardsProperty);
    }

    public Reward getFirstReward() {return rewards.getFirst();}

    public void sendTeamMessage(Component message) {
        for (var player : getOnlinePlayers())
            ChatHandler.sendMessage(player, message);
    }

    public void sendUnclaimedRewardsMessage(UUID playerId){
        Messages.UnclaimedRewardsMessage.send(playerId);
    }

    //</editor-fold>



    //<editor-fold desc="Commands">

    public void join(ServerPlayer player) throws CommandSyntaxException {
        ftbTeam.join(player);
        ChatHandler.updateCommands(player);
    }

    public void leave(ServerPlayer player) throws CommandSyntaxException {
        ftbTeam.leave(player.getUUID());
        ChatHandler.updateCommands(player);
    }

    public void delete(CommandSourceStack from) throws CommandSyntaxException {
        for (var player : getOnlinePlayers())
            ChatHandler.updateCommands(player);
        if (hasCore())
            removeCore();
        ftbTeam.forceDisband(from);
    }

    public void sendInvite(ServerPlayer player){
        if (invites.containsKey(player.getUUID()))
            invites.replace(player.getUUID(), this);
        else
            invites.put(player.getUUID(), this);

        ChatHandler.sendMessage(player, Messages.TeamInvitation.getTranslation().append(ftbTeam.getName()));
    }

    public static AcceptReturn acceptInvite(ServerPlayer player){
        if (!invites.containsKey(player.getUUID()))
            return AcceptReturn.NoInvite;
        if (get(player).isPresent())
            return AcceptReturn.AlreadyInTeam;

        var team = invites.get(player.getUUID());
        if(team.isFull())
            return AcceptReturn.TeamIsFull;

        try {
            team.join(player);
        } catch (CommandSyntaxException e) {
            return AcceptReturn.FailedForUnknownReason;
        }

        return AcceptReturn.Success;
    }

    //</editor-fold>




    @SubscribeEvent
    static void onServerStartup(ServerStartingEvent event) {
        FTBChunksWorldConfig.PARTY_LIMIT_MODE.set(PartyLimitMode.OWNER);
        FTBChunksWorldConfig.MAX_CLAIMED_CHUNKS.set(defaultMaxClaims);
        FTBChunksWorldConfig.MAX_FORCE_LOADED_CHUNKS.set(defaultMaxForceLoads);
        FTBChunksWorldConfig.HARD_TEAM_CLAIM_LIMIT.set(hardMaxClaims);
        FTBChunksWorldConfig.HARD_TEAM_FORCE_LIMIT.set(hardMaxForceLoads);

        FTBChunksWorldConfig.onConfigChanged(true);
    }


    public enum AcceptReturn {
        Success,
        AlreadyInTeam,
        NoInvite,
        TeamIsFull,
        FailedForUnknownReason
    }
}
