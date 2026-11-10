package de.chaos_cash.aerotechsmp.block.entity.custom;

import de.chaos_cash.aerotechsmp.team.Team;
import dev.ftb.mods.ftbchunks.FTBChunksAPIImpl;
import dev.ftb.mods.ftbchunks.api.ClaimedChunk;
import dev.ftb.mods.ftbchunks.api.event.ClaimedChunkEvent;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import dev.ftb.mods.ftbteams.api.event.TeamEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ClaimBlockEntity extends ModBlockEntity {
    private Team team;
    private final Consumer<TeamEvent> teamDeletedListener = this::onTeamDeleted;
    private final ClaimedChunkEvent.After unclaimListener = this::onChunkUnclaim;
    private final ClaimedChunkEvent.After claimListener = this::onChunkClaim;
    private boolean listenersRegistered = false;
    private ChunkDimPos chunkPos;



    public ClaimBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }



    private void onTeamDeleted(TeamEvent teamEvent) {
        if (team != null && teamEvent.getTeam().getTeamId() == team.getId())
            onTeamChange(team, null);
    }

    private void onChunkUnclaim(CommandSourceStack stack, ClaimedChunk claimedChunk) {
        if (getChunkPos() == null) return;
        if (claimedChunk.getPos().equals(getChunkPos()))
            onTeamChange(team, null);
    }

    private void onChunkClaim(CommandSourceStack stack, ClaimedChunk claimedChunk) {
        if (getChunkPos() == null) return;
        if (claimedChunk.getPos().equals(getChunkPos()))
            onTeamChange(team, Team.get(claimedChunk.getTeamData().getTeam().getTeamId()).orElse(null));
    }

    private ChunkDimPos getChunkPos() {
        if (chunkPos == null)
            if (level != null)
                chunkPos = new ChunkDimPos(level, getBlockPos());

        return chunkPos;
    }

    protected void onTeamChange(@Nullable Team oldTeam, @Nullable Team newTeam) {
        team = newTeam;
    }

    public Team getTeam() {
        return team;
    }


    @Override
    public void onLoad() {
        super.onLoad();
        registerListeners();
        team = getTeam(getBlockPos());
    }


    @Override
    public void setRemoved() {
        super.setRemoved();
        unRegisterListeners();
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        unRegisterListeners();
    }

    void unRegisterListeners() {
        if (!listenersRegistered) return;
        listenersRegistered = false;

        Runnable unregister = () -> {
            TeamEvent.DELETED.unregister(teamDeletedListener);
            ClaimedChunkEvent.AFTER_UNCLAIM.unregister(unclaimListener);
            ClaimedChunkEvent.AFTER_CLAIM.unregister(claimListener);
        };

        var server = level != null ? level.getServer() : null;
        if (server != null)
            server.execute(unregister);
        else
            unregister.run();
    }

    void registerListeners() {
        if (listenersRegistered) return;
        TeamEvent.DELETED.register(teamDeletedListener);
        ClaimedChunkEvent.AFTER_UNCLAIM.register(unclaimListener);
        ClaimedChunkEvent.AFTER_CLAIM.register(claimListener);
        listenersRegistered = true;
    }




    public static Team getTeam(@NotNull BlockPos pos) {
        var claimedChunks = FTBChunksAPIImpl.INSTANCE.getManager().getAllClaimedChunks();
        var chunkPos = new ChunkPos(pos);
        var claimedChunkOfBlock = claimedChunks.stream().filter((claimedChunk) -> claimedChunk.getPos().chunkPos().equals(chunkPos)).findAny();
        if (claimedChunkOfBlock.isEmpty()) return null;

        var optTeam = Team.get(claimedChunkOfBlock.get().getTeamData().getTeamId());

        return optTeam.orElse(null);
    }


    @Override
    protected void saveAdditionalData(CompoundTag tag, HolderLookup.Provider registries) {
    }


    @Override
    protected void loadAdditionalData(CompoundTag tag, HolderLookup.Provider registries) {
    }
}
