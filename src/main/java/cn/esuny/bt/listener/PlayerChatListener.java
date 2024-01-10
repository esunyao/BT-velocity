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
        // ��ȡ�����Ϣ
        Player player = playerChatEvent.getPlayer();
        // ��ȡ����ǳ�
        String playerUsername = player.getUsername();
        // ��ȡ�������ǳ�
        String serverName = player.getCurrentServer().get().getServer().getServerInfo().getName();
//        String serverName = player.getCurrentServer().get().getServer().getServerInfo().getName();
        // ��ȡ��ҷ��͵���Ϣ
        String playerMessage = MinecraftColorCodeUtil.replaceColorCode(playerChatEvent.getMessage());
//        String playerMessage = MinecraftColorCodeUtil.replaceColorCode(playerChatEvent.getMessage());
        // ��ȡ�����Ϣ�ĳ���
        int playerMessageLength = playerMessage.length();
        // ��ȡ�����ļ�
        LoadConfig loadConfig = new LoadConfig();
        // ��ȡ MCDR ����ǰ׺���б�
        List<Object> mcdrCommandPrefixList = loadConfig.getMcdrCommandPrefix();
        // ��ʼ��������� MCDR ����ǰ׺
        String finalMcdrCommandPrefix = null;
        // �� MCDR ����ǰ׺�б���е���
        for (Object mcdrCommandPrefix : mcdrCommandPrefixList) {
            // ��ȡ MCDR ����ǰ׺���ֵĳ���
            int mcdrCommandPrefixLength = mcdrCommandPrefix.toString().length();
            // ��ʼ������ MCDR ���������ȡ�����Ϣ
            String playerMessageSubMcdrCommandPrefix = null;
            // �п��ܻᷢ���ַ����±�Խ���쳣����Ҫ�򵥵Ĵ���һ��
            if (playerMessageLength > mcdrCommandPrefixLength) {
                // ����ҷ��͵���Ϣ��ͷ��ȡ�� MCDR ����ǰ׺���ֵ���ͬ����
                playerMessageSubMcdrCommandPrefix = playerMessage.substring(0, mcdrCommandPrefixLength);
            }
            // �����ȡ�����Ϣ��ΪΪ�ղ��ҽ�ȡ��ҵ���Ϣ�� MCDR ����ǰ׺��ͬ
            if (playerMessageSubMcdrCommandPrefix != null && playerMessageSubMcdrCommandPrefix.equals(mcdrCommandPrefix)) {
                // ��ȡ MCDR ����ǰ׺
                finalMcdrCommandPrefix = mcdrCommandPrefix.toString();
            }
        }
        // ���������� MCDR ����ǰ׺Ϊ��
        if (finalMcdrCommandPrefix == null) {
            // ȡ����Ϣ����
//            playerChatEvent.setResult(PlayerChatEvent.ChatResult.denied());
//            playerChatEvent.setResult(PlayerChatEvent.ChatResult.denied());
//            BT.logger.error("2222222");
            // ��ȡ�����ļ�����ǰ׺
            String mainPrefix = loadConfig.getMainPrefix();
            // ��ȡ���������ļ����ӷ����ƺ��ӷ�ǰ׺
            Map<String, Object> configServerList = loadConfig.getConfigServerList();
            // ��ȡ�����ļ��з����������ӷ�����
            Set<String> configServers = configServerList.keySet();
            // ��������ļ��ķ��������ư�����������ӷ�����������
            if (configServers.contains(serverName)) {
//                BT.logger.error("333333");
                // ��ȡ�ӷ���ǰ׺
                Object subPrefix = configServerList.get(serverName);
                // �����з��������ʹ����������Ϣ
//                proxyServer.get().forEach(registeredServer -> registeredServer.sendMessage(Component.text(subPrefix + " " + mainPrefix + " ��r<" + playerUsername + "> " + playerMessage)));
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
