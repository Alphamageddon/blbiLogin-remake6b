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

public class TpaCancel implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)){
            sender.sendMessage("Only players can use this command");
            return true;
        }
        TeleportRequest req = null;
        for(TeleportRequest r : Configvar.tpaRequests.values()){
            if(r.requester.equals(player.getName())){
                req = r; break;
            }
        }
        if(req == null){
            player.sendMessage("No pending request");
            return true;
        }
        Configvar.tpaRequests.remove(req.target);
        player.sendMessage("Teleport request cancelled");
        Player target = Bukkit.getPlayer(req.target);
        if(target != null) target.sendMessage(player.getName() + " cancelled the teleport request");
        return true;
    }
}
