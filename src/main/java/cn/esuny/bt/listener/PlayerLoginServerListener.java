package cn.esuny.bt.listener;

import cn.esuny.bt.BT;
import cn.esuny.bt.core.service.WebSocketService;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import cn.esuny.bt.config.LoadConfig;

import java.util.Map;
import java.util.NoSuchElementException;

import static java.lang.String.format;

public class PlayerLoginServerListener {
    private final ProxyServer proxyServer;

    public PlayerLoginServerListener(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    @Subscribe
    public void playerConnectionServer(ServerConnectedEvent connectedEvent) {
        try {
            // 获取玩家信息
            Player player = connectedEvent.getPlayer();
            // 获取玩家昵称
            String playerUsername = player.getUsername();
            // 获取玩家连接的服务器的名称
            String serverName = connectedEvent.getServer().getServerInfo().getName();
            //初始化上一个服务器名称
            String previousServerName = null;
//            BT.logger.error("c1");
            try {
                // 获取上一个服务器的名称
                previousServerName = connectedEvent.getPreviousServer().get().getServerInfo().getName();
            } catch (NoSuchElementException noSuchElement) {
//                BT.logger.error("e1");
            }
            // 读取配置文件
            LoadConfig loadConfig = new LoadConfig();
            // 获取配置文件的服务器名称及前缀
            Map<String, Object> configServerList = loadConfig.getConfigServerList();
            //如果配置服务器的列表包含玩家连接的服务器并且上一个服务器名称为空
//            BT.logger.error("c2");
//            BT.logger.error(serverName);
//            BT.logger.error(configServerList.toString());
            if (configServerList.containsKey(serverName) && previousServerName == null) {
                // 获取子服前缀
                Object subPrefix = configServerList.get(serverName);
                // 向所有的服务器发送玩家连接到服务器的消息
//                BT.logger.error("S1");
//            proxyServer.getAllServers().forEach(server -> {
//                server.sendMessage(Component.text("§8[§2+§8]§r " + playerUsername + " §2通过群组加入了§r " + subPrefix));
//            });
//            player.sendMessage(Component.text("§8[§2+§8]§r " + playerUsername + " §2通过群组加入了§r " + subPrefix));
                WebSocketService.send_dict(String.format("{\"Mode\": \"PlayerJoinEvent\", \"player\": \"%s\", \"server\": \"%s\"}", playerUsername, subPrefix));
                if(!BT.player_personality.getBooleanValue(playerUsername, true))
                    player.sendMessage(Component.text("§a§l您以关闭聊天转发，q群可能无法接收到您发送的信息，使用/bt chat on开启此功能"));
            }
            // 如果配置文件的服务器包含玩家连接服务器的名称相同并且上个服务器名称不为空并且上个服务器名称在配置文件中
            if (configServerList.containsKey(serverName) && previousServerName != null && configServerList.containsKey(previousServerName)) {
                // 获取连接的子服前缀
                Object subPrefix = configServerList.get(serverName);
                // 获取上一个连接的子服前缀
                Object previousServerSubPrefix = configServerList.get(previousServerName);
                // 向所有的服务器发送玩家切换服务器的消息
//                BT.logger.error("s2");
//            player.sendMessage(Component.text("§8[§b?§8]§r " + playerUsername + " §2从§r " + previousServerSubPrefix + " §2切换到§r " + subPrefix));
                WebSocketService.send_dict(String.format("{\"Mode\": \"PlayerHandoffEvent\", \"player\": \"%s\", \"fromserver\": \"%s\", \"toserver\": \"%s\"}", playerUsername, previousServerSubPrefix, subPrefix));
            }
        } catch (Exception exception) {
            BT.logger.error(exception.toString());
        }
    }
}
