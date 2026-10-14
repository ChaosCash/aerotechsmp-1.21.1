package de.chaos_cash.aerotechsmp.team;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.chat.ChatHandler;
import dev.ftb.mods.ftbteams.FTBTeamsAPIImpl;
import dev.ftb.mods.ftbteams.api.TeamRank;
import dev.ftb.mods.ftbteams.data.PartyTeam;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Team extends SavedData {
    public static final int maxSize = 2;
    private static final Map<UUID, Team> teams = new HashMap<>();
    private static final Map<UUID, Team> invites = new HashMap<>();


    private final PartyTeam ftbTeam;


    private Team(PartyTeam ftbTeam){
        this.ftbTeam = ftbTeam;
        setDirty(true);

        teams.put(ftbTeam.getTeamId(), this);
    }


    public int getTeamSize() {return ftbTeam.getPlayersByRank(TeamRank.ALLY).size();}

    public boolean isFull() {return getTeamSize() >= maxSize;}


    public void sendInvite(ServerPlayer player){
        if (invites.containsKey(player.getUUID()))
            invites.replace(player.getUUID(), this);
        else
            invites.put(player.getUUID(), this);

        ChatHandler.sendMessage(player, "You have been invited to the Team: " + ftbTeam.getName());
    }



    public List<ServerPlayer> getOnlinePlayers(){
        return ftbTeam.getOnlineMembers();
    }


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
        ftbTeam.forceDisband(from);
    }

    public boolean isOwner(ServerPlayer player){
        return ftbTeam.isOwner(player.getUUID());
    }

    public String getDisplayName() {
        return ftbTeam.getDisplayName();
    }


    static Team load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        UUID teamID = tag.getUUID("ftbTeam");

        var team = FTBTeamsAPIImpl.INSTANCE.getManager().getTeamByID(teamID);
        if (team.isEmpty())
            throw new RuntimeException("Team with ID: " + teamID + " does not exist!");

        return new Team((PartyTeam) team.get());
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


    public static Team create(ServerPlayer player, String name) throws CommandSyntaxException {
        PartyTeam ftbTeam = FTBTeamsAPIImpl.INSTANCE.getManager().createParty(player, name);

        var newTeam = getDS().computeIfAbsent(
            new Factory<>(
                    () -> new Team(ftbTeam),
                    Team::load
            ),
            "team_" + ftbTeam.getTeamId()
        );

        ChatHandler.updateCommands(player);

        return newTeam;
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

        return Optional.ofNullable(getDS().get(
                new Factory<>(
                        () -> null,
                        Team::load
                ),
                "team_" + teamID
        ));
    }


    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        compoundTag.putUUID("ftbTeam", ftbTeam.getTeamId());
        return compoundTag;
    }


    static DimensionDataStorage getDS() {return AerotechSMP.getServer().overworld().getDataStorage();}



    public enum AcceptReturn {
        Success,
        AlreadyInTeam,
        NoInvite,
        TeamIsFull,
        FailedForUnknownReason
    }
}
