package cn.esuny.bt;

import cn.esuny.bt.core.service.WebSocketAsyncService;
import cn.esuny.bt.core.service.WebSocketService;
import cn.esuny.bt.util.websocket.client.WebSocketClient;
import com.google.inject.Inject;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import cn.esuny.bt.listener.PlayerChatListener;
import cn.esuny.bt.listener.PlayerDisconnectListener;
import cn.esuny.bt.listener.PlayerLoginServerListener;

import java.net.URISyntaxException;

@Plugin(id = "bt", name = "BT", version = "1.0.0",
        authors = "esuny", url = "")
public class BT {

    @Inject public static Logger logger;

    public static ProxyServer proxyServer;
    private final ProxyServer proxy;
    public WebSocketAsyncService wbservice = new WebSocketAsyncService();

    @Inject
    public BT(ProxyServer proxyServer, Logger logger) {
        BT.proxyServer = proxyServer;
        this.proxy = proxyServer;
        BT.logger = logger;
        // ��ʼ�����
//        Initialization.init(logger);
        wbservice.start();
        logger.info("��aBTAPI�������");
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // ע�����������Ϣ������
        proxy.getEventManager().register(this, new PlayerChatListener(proxy));
        // ע��������ӷ�����������
        proxy.getEventManager().register(this, new PlayerLoginServerListener(proxy));
        // ע����ҶϿ�������������
        proxy.getEventManager().register(this, new PlayerDisconnectListener(proxy));
    }
}
