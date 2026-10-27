package de.chaos_cash.aerotechsmp.team;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.block.entity.custom.TeamCoreBlockEntity;
import de.chaos_cash.aerotechsmp.phases.PhaseHandler;
import de.chaos_cash.aerotechsmp.util.ChatHandler;
import de.chaos_cash.aerotechsmp.util.Messages;
import dev.architectury.event.CompoundEventResult;
import dev.ftb.mods.ftbchunks.api.ClaimResult;
import dev.ftb.mods.ftbchunks.api.ClaimedChunk;
import dev.ftb.mods.ftbchunks.api.event.ClaimedChunkEvent;
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

import java.awt.*;
import java.util.*;
import java.util.List;


@EventBusSubscriber(modid = AerotechSMP.MOD_ID)
public class Team {
    public static final int maxSize = 2;
    private static final Map<UUID, Team> teams = new HashMap<>();
    private static final Map<UUID, Team> invites = new HashMap<>();


    private static final BooleanProperty hasCorePropertyType = new BooleanProperty(ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID, "has_core"), false);
    private static final IntProperty corePosXPropertyType = new IntProperty(ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID, "core_pos_x"), null);
    private static final IntProperty corePosYPropertyType = new IntProperty(ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID, "core_pos_y"), null);
    private static final IntProperty corePosZPropertyType = new IntProperty(ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID, "core_pos_z"), null);
    private static final IntProperty maxClaimsPropertyType = new IntProperty(ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID, "max_claims"), 9);
    private static final IntProperty maxForceLoadsPropertyType = new IntProperty(ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID, "max_force_loads"), 0);
    private static final IntProperty forceLoadCountPropertyType = new IntProperty(ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID, "force_load_count"), 0);
    private static final IntProperty claimCountPropertyType = new IntProperty(ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID, "claim_count"), 0);
    private static final StringListProperty rewardsPropertyType = new StringListProperty(ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID, "rewards"), () -> null);


    private final PartyTeam ftbTeam;
    private final List<Reward> rewards = new ArrayList<>();
    private BlockPos corePos = null;
    private int maxClaims;
    private int maxForceLoads;
    private int claimCount;
    private int forceLoadCount;




    //<editor-fold desc="Creating / getting / loading">

    private Team(PartyTeam ftbTeam){
        this.ftbTeam = ftbTeam;

        teams.put(ftbTeam.getTeamId(), this);

        loadRewards();
        loadCore();

        maxClaims = ftbTeam.getProperty(maxClaimsPropertyType);
        maxForceLoads = ftbTeam.getProperty(maxForceLoadsPropertyType);
        claimCount = ftbTeam.getProperty(claimCountPropertyType);
        forceLoadCount = ftbTeam.getProperty(forceLoadCountPropertyType);
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

    public int getMaxClaims() {return maxClaims;}

    public int getMaxForceLoads() {return maxForceLoads;}

    public int getClaimCount() {
        return claimCount;
    }

    public int getForceLoadCount() {
        return forceLoadCount;
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
        maxClaims += increase;
        ftbTeam.setProperty(maxClaimsPropertyType, maxClaims);
    }

    public void increaseMaxForceLoads(int increase) {
        maxForceLoads += increase;
        ftbTeam.setProperty(maxForceLoadsPropertyType, maxForceLoads);
    }

    private void setClaimCount(int count) {
        claimCount = Math.max(count, 0);
        ftbTeam.setProperty(claimCountPropertyType, claimCount);
    }

    private void setForceLoadCount(int count) {
        forceLoadCount = Math.max(count, 0);
        ftbTeam.setProperty(forceLoadCountPropertyType, forceLoadCount);
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
        ClaimedChunkEvent.BEFORE_CLAIM.register(Team::onBeforeClaim);
        ClaimedChunkEvent.AFTER_CLAIM.register(Team::onAfterClaim);
        ClaimedChunkEvent.AFTER_UNCLAIM.register(Team::onAfterUnclaim);

        ClaimedChunkEvent.BEFORE_LOAD.register(Team::onBeforeLoad);
        ClaimedChunkEvent.AFTER_LOAD.register(Team::onAfterLoad);
        ClaimedChunkEvent.AFTER_UNLOAD.register(Team::onAfterUnload);

    }

    private static void onAfterUnload(CommandSourceStack stack, ClaimedChunk claimedChunk) {
        var optTeam = Team.get(claimedChunk.getTeamData().getTeam().getTeamId());
        if (optTeam.isEmpty()) return;
        Team team = optTeam.get();

        team.setForceLoadCount(team.getForceLoadCount() + 1);
    }

    private static void onAfterLoad(CommandSourceStack stack, ClaimedChunk claimedChunk) {
        var optTeam = Team.get(claimedChunk.getTeamData().getTeam().getTeamId());
        if (optTeam.isEmpty()) return;
        Team team = optTeam.get();

        team.setForceLoadCount(team.getForceLoadCount() - 1);
    }

    private static CompoundEventResult<ClaimResult> onBeforeLoad(CommandSourceStack stack, ClaimedChunk claimedChunk) {
        var optTeam = Team.get(claimedChunk.getTeamData().getTeam().getTeamId());
        if (optTeam.isEmpty()) return CompoundEventResult.interruptTrue(ClaimResult.customProblem(Messages.ActionRequiresTeam.key));

        Team team = optTeam.get();
        if (team.getForceLoadCount() >= team.getMaxForceLoads()) {
            Messages.ForceLoadLimitExceededLong.send(stack.getPlayer());
            return CompoundEventResult.interruptTrue(ClaimResult.customProblem(Messages.ForceLoadLimitExceeded.key));
        }
        else
            return CompoundEventResult.pass();
    }

    private static CompoundEventResult<ClaimResult> onBeforeClaim(CommandSourceStack stack, ClaimedChunk claimedChunk) {
        var optTeam = Team.get(claimedChunk.getTeamData().getTeam().getTeamId());
        if (optTeam.isEmpty()) return CompoundEventResult.interruptTrue(ClaimResult.customProblem(Messages.ActionRequiresTeam.key));

        Team team = optTeam.get();
        if (team.getClaimCount() >= team.getMaxClaims()) {
            Messages.ClaimLimitExceededLong.send(stack.getPlayer());
            return CompoundEventResult.interruptTrue(ClaimResult.customProblem(Messages.ClaimLimitExceeded.key));
        }
        else
            return CompoundEventResult.pass();
    }

    private static void onAfterUnclaim(CommandSourceStack commandSourceStack, ClaimedChunk claimedChunk) {
        var optTeam = Team.get(claimedChunk.getTeamData().getTeam().getTeamId());
        if (optTeam.isEmpty()) return;
        Team team = optTeam.get();

        team.setClaimCount(team.getClaimCount() - 1);
    }

    private static void onAfterClaim(CommandSourceStack commandSourceStack, ClaimedChunk claimedChunk) {
        var optTeam = Team.get(claimedChunk.getTeamData().getTeam().getTeamId());
        if (optTeam.isEmpty()) return;

        Team team = optTeam.get();
        team.setClaimCount(team.getClaimCount() + 1);
    }


    public enum AcceptReturn {
        Success,
        AlreadyInTeam,
        NoInvite,
        TeamIsFull,
        FailedForUnknownReason
    }
}
