package de.chaos_cash.aerotechsmp.block.entity.custom;

import de.chaos_cash.aerotechsmp.block.entity.ModBlockEntities;
import de.chaos_cash.aerotechsmp.team.Team;
import de.chaos_cash.aerotechsmp.util.ChatHandler;
import dev.ftb.mods.ftbchunks.FTBChunksAPIImpl;
import dev.ftb.mods.ftbchunks.api.ClaimedChunk;
import dev.ftb.mods.ftbchunks.api.event.ClaimedChunkEvent;
import dev.ftb.mods.ftbteams.api.event.TeamEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;


public class TeamCoreBlockEntity extends ModBlockEntity {
    Team team;

    public TeamCoreBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.TEAM_CORE_BLOCK_ENTITY.get(), pos, blockState);

        TeamEvent.DELETED.register(this::onTeamDeleted);
        ClaimedChunkEvent.AFTER_UNCLAIM.register(this::onChunkUnclaim);

        team = getTeam(pos);
        if (team == null)
            destroy();
        else
            onTeamChange();
    }

    private void onChunkUnclaim(CommandSourceStack stack, ClaimedChunk claimedChunk) {
        if (team == null)
            destroy();
        if (claimedChunk.getTeamData().getTeam().getTeamId() == team.getId())
            onTeamChange();
    }

    private void onTeamDeleted(TeamEvent teamEvent) {
        if (team == null)
            destroy();
        if (teamEvent.getTeam().getTeamId() == team.getId())
            onTeamChange();
    }


    @Override
    protected void saveAdditionalData(CompoundTag tag, HolderLookup.Provider registries) {
    }


    @Override
    protected void loadAdditionalData(CompoundTag tag, HolderLookup.Provider registries) {
    }


    void onTeamChange() {
        if (team != null && getTeam(getBlockPos()) == team) return;
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
        ChatHandler.broadcast(Component.literal("destroy"));
        var level = getLevel();
        if (level != null)
            if (!level.isClientSide()) {
                level.destroyBlock(getBlockPos(), false);
                if (team != null)
                    team.setHasCore(false);
            }
    }
}
