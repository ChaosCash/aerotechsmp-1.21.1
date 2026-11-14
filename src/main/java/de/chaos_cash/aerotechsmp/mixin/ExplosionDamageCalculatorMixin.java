package de.chaos_cash.aerotechsmp.mixin;

import de.chaos_cash.aerotechsmp.block.entity.custom.ShipProtectorBlockEntity;
import dev.ryanhcode.sable.companion.SableCompanion;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ExplosionDamageCalculator.class)
public class ExplosionDamageCalculatorMixin {

    @Inject(method = "getBlockExplosionResistance", at = @At("RETURN"), cancellable = true)
    public void getExplosionResistanceInjection(Explosion explosion, BlockGetter reader, BlockPos pos, BlockState state, FluidState fluid, CallbackInfoReturnable<Optional<Float>> cir) {
        var returnValue = cir.getReturnValue();
        if (returnValue.isEmpty()) return;

        var sourceEntity = explosion.getDirectSourceEntity();
        if (sourceEntity == null) return;

        var subLevel = SableCompanion.INSTANCE.getContaining(sourceEntity.level(), pos);
        if (subLevel == null) return;

        var protector = ShipProtectorBlockEntity.getForSubLevel(subLevel.getUniqueId());
        if (protector.isPresent()) {
            cir.setReturnValue(Optional.of(returnValue.get() + protector.get().getExplosionResistanceBonus()));
        }
    }
}
