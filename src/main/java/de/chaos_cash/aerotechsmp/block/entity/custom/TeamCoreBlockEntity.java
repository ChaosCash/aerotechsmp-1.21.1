package de.chaos_cash.aerotechsmp.block.entity.custom;

import de.chaos_cash.aerotechsmp.block.entity.ModBlockEntities;
import de.chaos_cash.aerotechsmp.team.Team;
import dev.ftb.mods.ftbchunks.FTBChunksAPIImpl;
import dev.ftb.mods.ftbchunks.api.ClaimedChunk;
import dev.ftb.mods.ftbchunks.api.event.ClaimedChunkEvent;
import dev.ftb.mods.ftbteams.api.event.TeamEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;


public class TeamCoreBlockEntity extends ModBlockEntity {
    private final Team team;
    private final Consumer<TeamEvent> teamDeletedListener = this::onTeamDeleted;
    private final ClaimedChunkEvent.After unclaimListener = this::onChunkUnclaim;
    private boolean listenersRegistered = false;

    public TeamCoreBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.TEAM_CORE_BLOCK_ENTITY.get(), pos, blockState);

        team = getTeam(pos);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (team != null)
            team.setHasCore(true);
        onTeamChange();
        registerListeners();
    }

    private void onChunkUnclaim(CommandSourceStack stack, ClaimedChunk claimedChunk) {
        if (team == null) {
            destroy();
            return;
        }
        if (claimedChunk.getTeamData().getTeam().getTeamId() == team.getId())
            onTeamChange();
    }

    private void onTeamDeleted(TeamEvent teamEvent) {
        if (team == null) {
            destroy();
            return;
        }
        if (teamEvent.getTeam().getTeamId() == team.getId())
            onTeamChange();
    }


    void onTeamChange() {
        if (team == null || getTeam(getBlockPos()) != team)
            destroy();
    }

    public static Team getTeam(@NotNull BlockPos pos) {
        var claimedChunks = FTBChunksAPIImpl.INSTANCE.getManager().getAllClaimedChunks();
        var chunkPos = new ChunkPos(pos);
        var claimedChunkOfBlock = claimedChunks.stream().filter((claimedChunk) -> claimedChunk.getPos().chunkPos().equals(chunkPos)).findAny();
        if (claimedChunkOfBlock.isEmpty()) return null;

        var optTeam = Team.get(claimedChunkOfBlock.get().getTeamData().getTeamId());

        return optTeam.orElse(null);
    }

    public Team getTeam() {return team;}


    void destroy(){
        var level = getLevel();
        if (level != null)
            if (!level.isClientSide()) {
                if (team != null)
                    team.setHasCore(false);
                level.destroyBlock(getBlockPos(), false);
            }
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
        listenersRegistered = true;
    }



    @Override
    protected void saveAdditionalData(CompoundTag tag, HolderLookup.Provider registries) {
    }


    @Override
    protected void loadAdditionalData(CompoundTag tag, HolderLookup.Provider registries) {
    }
}
