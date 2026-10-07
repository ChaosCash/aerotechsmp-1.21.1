package de.chaos_cash.aerotechsmp.phases;

import com.mojang.brigadier.context.CommandContext;
import de.chaos_cash.aerotechsmp.EventHandler;
import de.chaos_cash.aerotechsmp.chat.ChatHandler;
import net.creeperhost.ftbbackups.BackupHandler;
import net.creeperhost.ftbbackups.FTBBackups;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class PhaseHandler {


    public static int schedulePVPPhaseStartCommand(CommandContext<CommandSourceStack> context)
    {
        ChatHandler.Broadcast(Component.literal("Starting PVP phase in 60 seconds..."));

        createBackup("Pre PVP-Phase Backup ");

        EventHandler.scheduleTask(600, () -> {ChatHandler.Broadcast(Component.literal("Starting PVP phase in 30 seconds..."));});
        EventHandler.scheduleTask(1000, () -> {ChatHandler.Broadcast(Component.literal("Starting PVP phase in 10 seconds..."));});
        EventHandler.scheduleTask(1100, () -> {ChatHandler.Broadcast(Component.literal("Starting PVP phase in 5 seconds..."));});
        EventHandler.scheduleTask(1120, () -> {ChatHandler.Broadcast(Component.literal("Starting PVP phase in 4 seconds..."));});
        EventHandler.scheduleTask(1140, () -> {ChatHandler.Broadcast(Component.literal("Starting PVP phase in 3 seconds..."));});
        EventHandler.scheduleTask(1160, () -> {ChatHandler.Broadcast(Component.literal("Starting PVP phase in 2 seconds..."));});
        EventHandler.scheduleTask(1180, () -> {ChatHandler.Broadcast(Component.literal("Starting PVP phase in 1 seconds..."));});
        EventHandler.scheduleTask(1200, () -> {ChatHandler.Broadcast(Component.literal("Starting PVP phase..."));});

        return 1;
    }




    public static void createBackup()
    {
        createBackup("UnnamedBackup");
    }



    public static void createBackup(String name) {
        BackupHandler.createBackup(FTBBackups.minecraftServer, true, name);
    }
}
