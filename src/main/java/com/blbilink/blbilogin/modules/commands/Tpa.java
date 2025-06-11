package com.blbilink.blbilogin.modules.commands;

import com.blbilink.blbilogin.modules.tpa.TeleportRequest;
import com.blbilink.blbilogin.vars.Configvar;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.blbilink.blbilogin.BlbiLogin.plugin;

public class Tpa implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)){
            sender.sendMessage("Only players can use this command");
            return true;
        }
        if(args.length != 1){
            player.sendMessage("Usage: /" + command.getName() + " <player>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if(target == null){
            player.sendMessage("Player not online");
            return true;
        }
        if(target.equals(player)){
            player.sendMessage("Cannot teleport to yourself");
            return true;
        }
        boolean here = command.getName().equalsIgnoreCase("tpahere");
        TeleportRequest req = new TeleportRequest(player.getName(), target.getName(), here);
        Configvar.tpaRequests.put(target.getName(), req);
        player.sendMessage("Teleport request sent to " + target.getName());
        target.sendMessage(player.getName() + (here ? " wants you to teleport to them." : " wants to teleport to you."));
        target.sendMessage("Use /tpadecline to refuse. You have 10 seconds.");
        plugin.foliaUtil.runTaskLater(plugin, () -> executeRequest(req), 200L);
        return true;
    }

    private void executeRequest(TeleportRequest req){
        TeleportRequest stored = Configvar.tpaRequests.get(req.target);
        if(stored == null || !stored.requester.equals(req.requester)) return;
        Player requester = Bukkit.getPlayer(req.requester);
        Player target = Bukkit.getPlayer(req.target);
        if(requester == null || target == null) {
            Configvar.tpaRequests.remove(req.target);
            return;
        }
        if(req.here){
            Location loc = requester.getLocation();
            plugin.foliaUtil.runTask(plugin, task -> target.teleport(loc));
        }else{
            Location loc = target.getLocation();
            plugin.foliaUtil.runTask(plugin, task -> requester.teleport(loc));
        }
        Configvar.tpaRequests.remove(req.target);
    }
}
