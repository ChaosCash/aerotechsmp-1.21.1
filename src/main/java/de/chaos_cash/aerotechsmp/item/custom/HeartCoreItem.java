package de.chaos_cash.aerotechsmp.item.custom;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.team.RandomItemsReward;
import de.chaos_cash.aerotechsmp.team.Reward;
import de.chaos_cash.aerotechsmp.team.Team;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;


@EventBusSubscriber(modid = AerotechSMP.MOD_ID)
public class HeartCoreItem extends Item {
    static final CompoundTag defaultData = getDefaultData();
    static final int maxUseDuration = 40;
    static List<UUID> grantedRewards = new ArrayList<>();


    public HeartCoreItem(Properties properties) {
        super(properties);
    }


    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand usedHand) {
        if (getTag(player.getItemInHand(usedHand)).getInt("rarity") == 0 || grantedRewards.contains(player.getUUID()))
            return InteractionResultHolder.fail(player.getItemInHand(usedHand));

        player.startUsingItem(usedHand);
        return super.use(level, player, usedHand);
    }

    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity livingEntity, @NotNull ItemStack stack, int remainingUseDuration) {
        if (remainingUseDuration == 1 && livingEntity instanceof Player player){
            grantRewards(player, stack);
            player.stopUsingItem();
        }

        if (remainingUseDuration % 5 == 0)
            level.playLocalSound(livingEntity, SoundEvents.NOTE_BLOCK_BELL.value(), SoundSource.PLAYERS, 1, 1.5f - (float) remainingUseDuration / maxUseDuration);

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
        if (!grantedRewards.contains(player.getUUID())){
            team.addReward(new RandomItemsReward(rarity));
            grantedRewards.add(player.getUUID());
        }

        tag.putInt("rarity", 0);
        tag.putInt("charge_progress", 0);

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

        if (entity instanceof Player player) {

            var tag = getTag(stack);

            int curRarity = tag.getInt("rarity");
            int curProgress = tag.getInt("charge_progress") + 1;
            if (curProgress >= 100 && curRarity < 4) {
                curProgress = 0;
                curRarity += 1;
                tag.putInt("rarity", curRarity);
                grantedRewards.remove(player.getUUID());
            }

            tag.putInt("charge_progress", curProgress);
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }
    }


    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return maxUseDuration;
    }

    private static CompoundTag getDefaultData() {
        CompoundTag defaultData = new CompoundTag();

        defaultData.putInt("rarity", 0);
        defaultData.putInt("charge_progress", 0);

        return defaultData;
    }


    @SubscribeEvent
    static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event){
        grantedRewards.remove(event.getEntity().getUUID());
    }
}
