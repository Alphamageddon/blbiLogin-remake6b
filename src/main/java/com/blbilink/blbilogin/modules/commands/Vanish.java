package com.blbilink.blbilogin.modules.commands;

import com.blbilink.blbilogin.vars.Configvar;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.blbilink.blbilogin.BlbiLogin.plugin;

public class Vanish implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)){
            sender.sendMessage("Only players can use this command");
            return true;
        }
        if(!player.isOp()){
            player.sendMessage(plugin.i18n.as("msgNoPermission", true, player.getName()));
            return true;
        }
        if(Configvar.vanished.contains(player.getName())){
            Configvar.vanished.remove(player.getName());
            for(Player p : Bukkit.getOnlinePlayers()){
                p.showPlayer(plugin, player);
            }
            player.setInvisible(false);
            player.sendMessage("Vanish disabled");
        }else{
            Configvar.vanished.add(player.getName());
            for(Player p : Bukkit.getOnlinePlayers()){
                if(!p.equals(player)){
                    p.hidePlayer(plugin, player);
                }
            }
            player.setInvisible(true);
            player.sendMessage("Vanish enabled");
        }
        return true;
    }
}
