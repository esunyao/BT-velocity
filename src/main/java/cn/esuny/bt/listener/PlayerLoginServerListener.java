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
            // ��ȡ�����Ϣ
            Player player = connectedEvent.getPlayer();
            // ��ȡ����ǳ�
            String playerUsername = player.getUsername();
            // ��ȡ������ӵķ�����������
            String serverName = connectedEvent.getServer().getServerInfo().getName();
            //��ʼ����һ������������
            String previousServerName = null;
//            BT.logger.error("c1");
            try {
                // ��ȡ��һ��������������
                previousServerName = connectedEvent.getPreviousServer().get().getServerInfo().getName();
            } catch (NoSuchElementException noSuchElement) {
//                BT.logger.error("e1");
            }
            // ��ȡ�����ļ�
            LoadConfig loadConfig = new LoadConfig();
            // ��ȡ�����ļ��ķ��������Ƽ�ǰ׺
            Map<String, Object> configServerList = loadConfig.getConfigServerList();
            //������÷��������б����������ӵķ�����������һ������������Ϊ��
//            BT.logger.error("c2");
//            BT.logger.error(serverName);
//            BT.logger.error(configServerList.toString());
            if (configServerList.containsKey(serverName) && previousServerName == null) {
                // ��ȡ�ӷ�ǰ׺
                Object subPrefix = configServerList.get(serverName);
                // �����еķ���������������ӵ�����������Ϣ
//                BT.logger.error("S1");
//            proxyServer.getAllServers().forEach(server -> {
//                server.sendMessage(Component.text("��8[��2+��8]��r " + playerUsername + " ��2ͨ��Ⱥ������ˡ�r " + subPrefix));
//            });
//            player.sendMessage(Component.text("��8[��2+��8]��r " + playerUsername + " ��2ͨ��Ⱥ������ˡ�r " + subPrefix));
                WebSocketService.send_dict(String.format("{\"Mode\": \"PlayerJoinEvent\", \"player\": \"%s\", \"server\": \"%s\"}", playerUsername, subPrefix));
                if(!BT.player_personality.getBooleanValue(playerUsername, true))
                    player.sendMessage(Component.text("��a��l���Թر�����ת����qȺ�����޷����յ������͵���Ϣ��ʹ��/bt chat on�����˹���"));
            }
            // ��������ļ��ķ���������������ӷ�������������ͬ�����ϸ����������Ʋ�Ϊ�ղ����ϸ������������������ļ���
            if (configServerList.containsKey(serverName) && previousServerName != null && configServerList.containsKey(previousServerName)) {
                // ��ȡ���ӵ��ӷ�ǰ׺
                Object subPrefix = configServerList.get(serverName);
                // ��ȡ��һ�����ӵ��ӷ�ǰ׺
                Object previousServerSubPrefix = configServerList.get(previousServerName);
                // �����еķ�������������л�����������Ϣ
//                BT.logger.error("s2");
//            player.sendMessage(Component.text("��8[��b?��8]��r " + playerUsername + " ��2�ӡ�r " + previousServerSubPrefix + " ��2�л�����r " + subPrefix));
                WebSocketService.send_dict(String.format("{\"Mode\": \"PlayerHandoffEvent\", \"player\": \"%s\", \"fromserver\": \"%s\", \"toserver\": \"%s\"}", playerUsername, previousServerSubPrefix, subPrefix));
            }
        } catch (Exception exception) {
            BT.logger.error(exception.toString());
        }
    }
}
