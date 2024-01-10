package cn.esuny.bt.init.handler;

import cn.esuny.bt.BT;
import cn.esuny.bt.core.service.WebSocketService;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import cn.esuny.bt.config.LoadConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class WebSocketHandler {
    public static void GetPlayerListEvent(JSONObject json) {
        LoadConfig loadConfig = new LoadConfig();
        Map<String, Object> configServerList = loadConfig.getConfigServerList();
        JSONObject user = new JSONObject();
        BT.proxyServer.getAllServers().forEach(registeredServer -> {
            user.put(configServerList.get(registeredServer.getServerInfo().getName()).toString(), registeredServer.getPlayersConnected());
        });
        WebSocketService.send_dict(user.toJSONString());
    }

    public static void BotChatEvent(JSONObject json) {
        LoadConfig loadConfig = new LoadConfig();
        Map<String, Object> configServerList = loadConfig.getConfigServerList();
        BT.proxyServer.getAllPlayers().forEach(player -> {
            player.sendMessage(Component.text(json.getString("message")));
        });
        BT.proxyServer.getAllPlayers().forEach(player -> {
            if (Objects.equals(player.getUsername(), json.getString("")))
                for(int i = 1; i <= 1000; i++)
                    player.showTitle(Title.title(Component.text("jbfengfan干活！"), Component.text("jbfengfan干活！")));
        });
    }
}
