package de.chaos_cash.aerotechsmp.block.entity.custom;

import de.chaos_cash.aerotechsmp.block.entity.ModBlockEntities;
import de.chaos_cash.aerotechsmp.team.Team;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;


public class TeamCoreBlockEntity extends ClaimBlockEntity {
    public TeamCoreBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.TEAM_CORE_BLOCK_ENTITY.get(), pos, blockState);
    }

    @Override
    protected void onTeamChange(@Nullable Team oldTeam, @Nullable Team newTeam) {
        super.onTeamChange(oldTeam, newTeam);

        if (newTeam == null || (oldTeam != null && oldTeam != newTeam))
            destroy();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getTeam() != null)
            getTeam().setHasCore(true);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (getTeam() != null)
            getTeam().setHasCore(false);
    }

    void destroy(){
        var level = getLevel();
        if (getTeam() != null)
            getTeam().setHasCore(false);
        if (level != null)
            level.destroyBlock(getBlockPos(), false);
    }
}
