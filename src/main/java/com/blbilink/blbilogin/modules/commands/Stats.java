package com.blbilink.blbilogin.modules.commands;

import com.blbilink.blbilogin.BlbiLogin;
import com.blbilink.blbilogin.vars.Configvar;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Stats implements CommandExecutor {
    private final BlbiLogin plugin = BlbiLogin.plugin;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)){
            sender.sendMessage("Only players can use this command");
            return true;
        }
        long login = Configvar.loginTime.getOrDefault(player.getName(), player.getFirstPlayed());
        long uptimeMillis = System.currentTimeMillis() - login;
        long uptimeSecs = uptimeMillis / 1000;
        long hours = uptimeSecs / 3600;
        long minutes = (uptimeSecs % 3600) / 60;
        long seconds = uptimeSecs % 60;

        long sizeBytes = getDirectorySize(Bukkit.getWorldContainer());
        double sizeGB = sizeBytes / 1024.0 / 1024.0 / 1024.0;

        LocalDate start = LocalDate.of(2025,5,5);
        long days = ChronoUnit.DAYS.between(start, LocalDate.now());

        int totalPlayers = Bukkit.getOfflinePlayers().length;

        player.sendMessage("Uptime: " + String.format("%02d:%02d:%02d", hours, minutes, seconds));
        player.sendMessage(String.format("World size: %.2f GB", sizeGB));
        player.sendMessage("World age: " + days + " days");
        player.sendMessage("Players joined: " + totalPlayers);
        return true;
    }

    private long getDirectorySize(File dir){
        long size = 0;
        File[] files = dir.listFiles();
        if(files != null){
            for(File f : files){
                if(f.isFile()) size += f.length();
                else size += getDirectorySize(f);
            }
        }
        return size;
    }
}
