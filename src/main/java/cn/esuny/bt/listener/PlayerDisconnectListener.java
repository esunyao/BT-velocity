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
            // ��ȡ�����Ϣ
            Player player = event.getPlayer();
            // ��ȡ����ǳ�
            String playerUsername = player.getUsername();
            // ������˳�Ⱥ�����Ϣ���͸�������
//        proxyServer.getAllServers().forEach(server -> {
//            server.sendMessage(Component.text("��8[��c-��8]��r " + playerUsername + " ��2�뿪��Ⱥ��"));
//        });
            WebSocketService.send_dict(String.format("{\"Mode\": \"PlayerLeftEvent\", \"player\": \"%s\"}", playerUsername));
        }catch (Exception exception){
            BT.logger.error(exception.toString());
        }
    }
}
