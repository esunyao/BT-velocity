package cn.esuny.bt.listener;

import cn.esuny.bt.BT;
import cn.esuny.bt.core.service.WebSocketService;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import cn.esuny.bt.config.LoadConfig;
import cn.esuny.bt.util.MinecraftColorCodeUtil;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class PlayerChatListener {
    private final ProxyServer proxyServer;

    public PlayerChatListener(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }


    @Subscribe
    public void playerChatEvent(PlayerChatEvent playerChatEvent) {
//        BT.logger.error("111111111111111111111");
        // 获取玩家信息
        Player player = playerChatEvent.getPlayer();
        // 获取玩家昵称
        String playerUsername = player.getUsername();
        // 获取服务器昵称
        String serverName = player.getCurrentServer().get().getServer().getServerInfo().getName();
//        String serverName = player.getCurrentServer().get().getServer().getServerInfo().getName();
        // 获取玩家发送的消息
        String playerMessage = MinecraftColorCodeUtil.replaceColorCode(playerChatEvent.getMessage());
//        String playerMessage = MinecraftColorCodeUtil.replaceColorCode(playerChatEvent.getMessage());
        // 获取玩家消息的长度
        int playerMessageLength = playerMessage.length();
        // 读取配置文件
        LoadConfig loadConfig = new LoadConfig();
        // 获取 MCDR 命令前缀的列表
        List<Object> mcdrCommandPrefixList = loadConfig.getMcdrCommandPrefix();
        // 初始化迭代后的 MCDR 命令前缀
        String finalMcdrCommandPrefix = null;
        // 将 MCDR 命令前缀列表进行迭代
        for (Object mcdrCommandPrefix : mcdrCommandPrefixList) {
            // 获取 MCDR 命令前缀文字的长度
            int mcdrCommandPrefixLength = mcdrCommandPrefix.toString().length();
            // 初始化根据 MCDR 命令长度来截取玩家消息
            String playerMessageSubMcdrCommandPrefix = null;
            // 有可能会发生字符串下标越界异常，需要简单的处理一下
            if (playerMessageLength > mcdrCommandPrefixLength) {
                // 将玩家发送的消息从头截取与 MCDR 命令前缀文字的相同长度
                playerMessageSubMcdrCommandPrefix = playerMessage.substring(0, mcdrCommandPrefixLength);
            }
            // 如果截取玩家消息不为为空并且截取玩家的消息和 MCDR 命令前缀相同
            if (playerMessageSubMcdrCommandPrefix != null && playerMessageSubMcdrCommandPrefix.equals(mcdrCommandPrefix)) {
                // 获取 MCDR 命令前缀
                finalMcdrCommandPrefix = mcdrCommandPrefix.toString();
            }
        }
        // 如果迭代后的 MCDR 命令前缀为空
        if (finalMcdrCommandPrefix == null) {
            // 取消消息发送
//            playerChatEvent.setResult(PlayerChatEvent.ChatResult.denied());
//            playerChatEvent.setResult(PlayerChatEvent.ChatResult.denied());
//            BT.logger.error("2222222");
            // 获取配置文件的主前缀
            String mainPrefix = loadConfig.getMainPrefix();
            // 获取所有配置文件的子服名称和子服前缀
            Map<String, Object> configServerList = loadConfig.getConfigServerList();
            // 获取配置文件中服务器所有子服名称
            Set<String> configServers = configServerList.keySet();
            // 如果配置文件的服务器名称包含玩家所连接服务器的名称
            if (configServers.contains(serverName)) {
//                BT.logger.error("333333");
                // 获取子服的前缀
                Object subPrefix = configServerList.get(serverName);
                // 向所有服务器发送处理后的玩家消息
//                proxyServer.get().forEach(registeredServer -> registeredServer.sendMessage(Component.text(subPrefix + " " + mainPrefix + " §r<" + playerUsername + "> " + playerMessage)));
                try {
                    if(BT.player_personality.getBooleanValue(playerUsername, true))
                        WebSocketService.send_dict(String.format("{\"Mode\": \"PlayerChatEvent\", \"player\": \"%s\", \"message\": \"%s\", \"server\": \"%s\"}", playerUsername, playerMessage, subPrefix));
                } catch (Exception exception) {
                    BT.logger.error(exception.toString());
                    BT.logger.warn(String.format("{\"Mode\": \"PlayerChatEvent\", \"player\": \"%s\", \"message\": \"%s\", \"server\": \"%s\"}", playerUsername, playerMessage, subPrefix));
                }
            }
        }
    }
}
