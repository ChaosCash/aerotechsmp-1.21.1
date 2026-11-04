package de.chaos_cash.aerotechsmp.team;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.util.ChatHandler;
import de.chaos_cash.aerotechsmp.util.Messages;
import dev.ftb.mods.ftbchunks.FTBChunksAPIImpl;
import dev.ftb.mods.ftbchunks.FTBChunksWorldConfig;
import dev.ftb.mods.ftbchunks.data.ChunkTeamDataImpl;
import dev.ftb.mods.ftbchunks.data.PartyLimitMode;
import dev.ftb.mods.ftbteams.FTBTeamsAPIImpl;
import dev.ftb.mods.ftbteams.api.event.PlayerJoinedPartyTeamEvent;
import dev.ftb.mods.ftbteams.data.PartyTeam;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.List;



@EventBusSubscriber(modid = AerotechSMP.MOD_ID)
public class Team {
    public static final int maxPartySize = 2;
    public static final int defaultMaxClaims = 9;
    public static final int defaultMaxForceLoads = 0;
    public static final int hardMaxClaims = 500;
    public static final int hardMaxForceLoads = 16;
    private static final Map<UUID, Team> teams = new HashMap<>();

    private final @NotNull PartyTeam ftbTeam;
    private final ChunkTeamDataImpl chunkTeamData;
    private final ChunkTeamDataImpl ownerData;




    //<editor-fold desc="Creating / getting / loading">

    private Team(PartyTeam ftbTeam){
        this.ftbTeam = ftbTeam;

        teams.put(ftbTeam.getTeamId(), this);

        chunkTeamData = FTBChunksAPIImpl.INSTANCE.getManager().getOrCreateData(ftbTeam);
        ownerData = FTBChunksAPIImpl.INSTANCE.getManager().getPersonalData(getOwner());
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

    public int getMaxClaims() {return chunkTeamData.getMaxClaimChunks();}

    public int getMaxForceLoads() {return chunkTeamData.getMaxForceLoadChunks();}

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

    //</editor-fold>





    @SubscribeEvent
    static void onServerStartup(ServerStartingEvent event) {
        FTBChunksWorldConfig.PARTY_LIMIT_MODE.set(PartyLimitMode.OWNER);
        FTBChunksWorldConfig.MAX_CLAIMED_CHUNKS.set(defaultMaxClaims);
        FTBChunksWorldConfig.MAX_FORCE_LOADED_CHUNKS.set(defaultMaxForceLoads);
        FTBChunksWorldConfig.HARD_TEAM_CLAIM_LIMIT.set(hardMaxClaims);
        FTBChunksWorldConfig.HARD_TEAM_FORCE_LIMIT.set(hardMaxForceLoads);

        FTBChunksWorldConfig.onConfigChanged(true);


        PlayerJoinedPartyTeamEvent.PLAYER_JOINED_PARTY.register(Team::onPartyJoined);
    }

    private static void onPartyJoined(PlayerJoinedPartyTeamEvent event) {
        var team = event.getTeam();
        var player = event.getPlayer();
        if (team instanceof PartyTeam partyTeam)
            if (partyTeam.getMembers().size() > maxPartySize)
                partyTeam.removeMember(player.getUUID());

        Messages.TeamAlreadyFull.send(player);
    }

}
