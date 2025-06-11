package com.blbilink.blbilogin.modules.commands;

import com.blbilink.blbilogin.modules.tpa.TeleportRequest;
import com.blbilink.blbilogin.vars.Configvar;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.blbilink.blbilogin.BlbiLogin.plugin;

public class TpaDecline implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)){
            sender.sendMessage("Only players can use this command");
            return true;
        }
        TeleportRequest req = Configvar.tpaRequests.get(player.getName());
        if(req == null){
            player.sendMessage("No request to decline");
            return true;
        }
        Configvar.tpaRequests.remove(player.getName());
        player.sendMessage("Teleport request declined");
        Player requester = Bukkit.getPlayer(req.requester);
        if(requester != null) requester.sendMessage(player.getName() + " declined your teleport request");
        return true;
    }
}
