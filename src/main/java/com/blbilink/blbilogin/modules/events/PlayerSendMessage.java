package com.blbilink.blbilogin.modules.events;

import com.blbilink.blbilogin.BlbiLogin;
import com.blbilink.blbilogin.vars.Configvar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerSendMessage implements Listener {
    private BlbiLogin plugin = BlbiLogin.plugin;
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        ifCantSendMessage(e, player);
        checkSpam(e, player);
    }

    private void ifCantSendMessage(AsyncPlayerChatEvent event,Player player) {
        if(Configvar.config.getBoolean("noLoginPlayerCantSendMessage") && Configvar.noLoginPlayerList.contains(event.getPlayer().getName())){
            event.setCancelled(true);
        }
    }

    private void checkSpam(AsyncPlayerChatEvent event, Player player){
        String msg = event.getMessage().trim();
        String last = Configvar.lastMessage.get(player.getName());
        int count = Configvar.sameMessageCount.getOrDefault(player.getName(), 0);
        if(msg.equalsIgnoreCase(last)){
            count++;
        }else{
            count = 1;
        }
        Configvar.lastMessage.put(player.getName(), msg);
        Configvar.sameMessageCount.put(player.getName(), count);
        if(count >= 5){
            event.setCancelled(true);
            plugin.foliaUtil.runTaskForEntity(player, plugin, () -> player.kickPlayer("Spamming messages"), null, 0L);
        }
    }
}
