package de.chaos_cash.aerotechsmp;

import de.chaos_cash.aerotechsmp.chat.ChatHandler;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = AerotechSMP.MODID)
public class EventHandler {
        private static final List<ScheduledTask> TASKS = new ArrayList<>();


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
                TASKS.add(new ScheduledTask(delayInTicks, action));
            }
        }



        @SubscribeEvent
        public static void onServerTick(ServerTickEvent.Post event) {
            synchronized (TASKS) {
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

}
