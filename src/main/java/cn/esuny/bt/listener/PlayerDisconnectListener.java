package cn.esuny.bt.listener;

import cn.esuny.bt.BT;
import cn.esuny.bt.core.service.WebSocketService;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;

public class PlayerDisconnectListener {
    private final ProxyServer proxyServer;

    public PlayerDisconnectListener(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    @Subscribe
    public void playerDisconnectServer(DisconnectEvent event) {
        try {
            // 获取玩家信息
            Player player = event.getPlayer();
            // 获取玩家昵称
            String playerUsername = player.getUsername();
            // 将玩家退出群组的消息发送给所有人
//        proxyServer.getAllServers().forEach(server -> {
//            server.sendMessage(Component.text("§8[§c-§8]§r " + playerUsername + " §2离开了群组"));
//        });
            WebSocketService.send_dict(String.format("{\"Mode\": \"PlayerLeftEvent\", \"player\": \"%s\"}", playerUsername));
        }catch (Exception exception){
            BT.logger.error(exception.toString());
        }
    }
}
