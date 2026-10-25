package de.chaos_cash.aerotechsmp.item.custom;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.item.ModItems;
import de.chaos_cash.aerotechsmp.team.RandomItemsReward;
import de.chaos_cash.aerotechsmp.team.Reward;
import de.chaos_cash.aerotechsmp.team.Team;
import de.chaos_cash.aerotechsmp.util.ChatHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;


@EventBusSubscriber(modid = AerotechSMP.MOD_ID)
public class HeartCoreItem extends Item {
    static final CompoundTag defaultData = getDefaultData();
    static final int maxUseDuration = 40;
    static final int baseChargeDuration = 6000;
    static final int tooltipProgressBarCount = 50;


    public HeartCoreItem(Properties properties) {
        super(properties);
    }


    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        if (!level.isClientSide()) {
            if (getTag(player.getItemInHand(usedHand)).getInt("rarity") == 0)
                return InteractionResultHolder.fail(player.getItemInHand(usedHand));

            player.startUsingItem(usedHand);
        }

        return super.use(level, player, usedHand);
    }


    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity livingEntity, @NotNull ItemStack stack, int remainingUseDuration) {
        if (!level.isClientSide()) {
            if (remainingUseDuration == 1 && livingEntity instanceof Player player) {
                grantRewards(player, stack);
                player.stopUsingItem();
            }

            if (remainingUseDuration % 5 == 0)
                level.playSound(
                    null,
                    livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                    SoundEvents.NOTE_BLOCK_BELL.value(),
                    SoundSource.PLAYERS,
                    1,
                    1.5f - (float) remainingUseDuration / maxUseDuration
                );
        }

        super.onUseTick(level, livingEntity, stack, remainingUseDuration);
    }


    private void grantRewards(Player player, ItemStack stack) {
        var tag = getTag(stack);
        var rarity = Reward.Rarity.fromInt(tag.getInt("rarity"));

        if (rarity == Reward.Rarity.Common)
            return;

        var optTeam = Team.get(player);
        if (optTeam.isEmpty()) return;

        var team = optTeam.get();
        team.addReward(new RandomItemsReward(rarity));

        tag.putInt("rarity", 0);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }


    private static CompoundTag getTag(ItemStack stack){
        var customData = stack.get(DataComponents.CUSTOM_DATA);
        CompoundTag tag;
        if (customData == null)
            tag = defaultData;
        else
            tag = customData.copyTag();

        return tag;
    }


    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (!level.isClientSide() && entity instanceof Player) {

            var tag = getTag(stack);

            int curRarity = tag.getInt("rarity");
            int chargeSpeed = tag.getInt("charge_speed");
            int curProgress = tag.getInt("charge_progress") + chargeSpeed;
            int chargeToNextRarity = (int) (baseChargeDuration * Reward.Rarity.fromInt(curRarity).getMultiplier());
            if (curRarity < 4 && curProgress >= chargeToNextRarity) {
                curProgress -= chargeToNextRarity;
                curRarity += 1;
                tag.putInt("rarity", curRarity);
            }

            tag.putInt("charge_progress", curProgress);
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }
    }


    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.NONE;
    }


    @Override
    public boolean shouldCauseReequipAnimation(@NotNull ItemStack oldStack, @NotNull ItemStack newStack, boolean slotChanged) {
        return false;
    }


    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return maxUseDuration;
    }


    private static CompoundTag getDefaultData() {
        CompoundTag defaultData = new CompoundTag();

        defaultData.putInt("rarity", 0);
        defaultData.putInt("charge_progress", 0);
        defaultData.putInt("charge_speed", 1);

        return defaultData;
    }


    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        var tag = getTag(stack);
        var rarity = Reward.Rarity.fromInt(tag.getInt("rarity"));
        int chargeSpeed = tag.getInt("charge_speed");
        var chargeProgress = tag.getInt("charge_progress");
        int chargeTime = (int) (baseChargeDuration * rarity.getMultiplier());
        double chargePercent = (double) chargeProgress / chargeTime;
        int finishedBars = (int) (chargePercent * tooltipProgressBarCount);


        tooltipComponents.add(Component.literal(rarity.name()).withStyle(rarity.color));
        tooltipComponents.add(Component.literal(""));
        if (rarity != Reward.Rarity.Legendary) {
            tooltipComponents.add(Component.literal("Charging..."));
            if (chargeSpeed > 1)
                tooltipComponents.add(Component.literal("x" + chargeSpeed).withStyle(ChatFormatting.GREEN).append(Component.literal(" charge speed").withStyle(ChatFormatting.WHITE)));
            tooltipComponents.add(
                Component.literal("[")
                .append(Component.literal("|".repeat(finishedBars)).withStyle(ChatFormatting.GREEN))
                .append(Component.literal("|".repeat(tooltipProgressBarCount - finishedBars)).withStyle(ChatFormatting.GRAY))
                .append(Component.literal("]"))
            );

            tooltipComponents.add(Component.literal(""));
            tooltipComponents.add(Component.literal("Charge Time:"));
            tooltipComponents.add(Component.literal(ChatHandler.formatTickTime((chargeTime - chargeProgress) / chargeSpeed, true)));
        }
        else{
            tooltipComponents.add(Component.literal("Fully Charged!").withStyle(ChatFormatting.GREEN));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }


    private static void combine(@NotNull ItemStack stack, @NotNull ItemEntity entity) {
        var stackTag = getTag(stack);
        var otherTag = getTag(entity.getItem());
        Reward.Rarity otherRarity = Reward.Rarity.fromInt(otherTag.getInt("rarity"));
        int otherProgress = otherTag.getInt("charge_progress") + getAccumulatedProgress(otherRarity);

        stackTag.putInt("charge_progress", stackTag.getInt("charge_progress") + otherProgress);
        stackTag.putInt("charge_speed", stackTag.getInt("charge_speed") + otherTag.getInt("charge_speed"));
        stack.set(() -> DataComponents.CUSTOM_DATA, CustomData.of(stackTag));

        entity.kill();
    }


    static int getAccumulatedProgress(Reward.Rarity rarity) {
        int accumulatedProgress = 0;

        for (var curRarity : Reward.Rarity.values()){
            if (rarity != Reward.Rarity.Common)
                accumulatedProgress += (int) (baseChargeDuration * curRarity.getMultiplier());

            if (rarity.equals(curRarity))
                break;
        }

        return accumulatedProgress;
    }


    @SubscribeEvent
    static void onPrePickup(ItemEntityPickupEvent.Pre event) {
        if (event.getItemEntity().level().isClientSide()) return;

        var itemEntity = event.getItemEntity();
        if (!itemEntity.getItem().is(ModItems.HEART_CORE)) return;

        for (var stack : event.getPlayer().getInventory().items) {
            if (stack.is(ModItems.HEART_CORE)) {
                combine(stack, itemEntity);
                event.setCanPickup(TriState.FALSE);
                return;
            }
        }
    }
}
