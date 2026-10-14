package de.chaos_cash.aerotechsmp.util;


import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.team.Team;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = AerotechSMP.MOD_ID)
public class EventHandler {
        private static final List<ScheduledTask> TASKS = new ArrayList<>();
        private static final List<ScheduledTask> TASKS_TO_ADD = new ArrayList<>();


        private static class ScheduledTask {
            int ticksRemaining;
            final Runnable action;

            ScheduledTask(int ticksRemaining, Runnable action) {
                this.ticksRemaining = ticksRemaining;
                this.action = action;
            }
        }



        public static void scheduleTask(int delayInTicks, Runnable action) {
            if (delayInTicks <= 0) {
                action.run();
                return;
            }

            synchronized (TASKS) {
                TASKS_TO_ADD.add(new ScheduledTask(delayInTicks, action));
            }
        }



        @SubscribeEvent
        static void onServerTick(ServerTickEvent.Post event) {
            synchronized (TASKS) {
                if (!TASKS_TO_ADD.isEmpty())
                    while (!TASKS_TO_ADD.isEmpty()) {
                        var task = TASKS_TO_ADD.getFirst();
                        TASKS.add(task);
                        TASKS_TO_ADD.remove(task);
                    }

                if (TASKS.isEmpty()) return;

                List<ScheduledTask> tasksToRemove = new ArrayList<>();
                for (var task : TASKS) {
                    task.ticksRemaining--;

                    if (task.ticksRemaining <= 0) {
                        try {
                            task.action.run();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        tasksToRemove.add(task);
                    }
                }

                if (tasksToRemove.isEmpty()) return;
                for (var task :tasksToRemove)
                    TASKS.remove(task);

            }
        }



        @SubscribeEvent
        static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event){
            Player player = event.getEntity();
            var team = Team.get(player);
            if (team.isEmpty())
                return;

            if (team.get().hasRewards())
                team.get().sendUnclaimedRewardsMessage(player.getUUID());
        }
}
