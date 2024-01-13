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
        user.put("Mode", "PlayerListEvent");
        user.put("group", json.get("group"));
        user.put("PlayerList", new JSONObject());
        BT.proxyServer.getAllPlayers().forEach(player -> {
            if (configServerList.getOrDefault(player.getCurrentServer().get().getServer().getServerInfo().getName(), null) == null) {
                user.getJSONObject("PlayerList").put(player.getUsername(), player.getCurrentServer().get().getServer().getServerInfo().getName());
            } else {
                user.getJSONObject("PlayerList").put(player.getUsername(), configServerList.get(player.getCurrentServer().get().getServer().getServerInfo().getName()));
            }
        });
//        BT.logger.error(user.toJSONString());
//        BT.proxyServer.getAllServers().forEach(registeredServer -> {
//            BT.logger.info(registeredServer.getServerInfo().getName());
//            try{
//            BT.logger.info(registeredServer.getPlayersConnected().);
//            }catch (Exception e){
//                BT.logger.error(e.toString());
//            }
////            user.put(configServerList.get(registeredServer.getServerInfo().getName()).toString(), registeredServer.getPlayersConnected());
//        });
        WebSocketService.send_dict(user.toJSONString());
    }

    public static void AllPlayerTitle(JSONObject json) {
        BT.proxyServer.getAllPlayers().forEach(player -> {
            player.showTitle(
                    Title.title(
                            Component.text(json.getString(json.getString("title"))),
                            Component.text(json.getString(json.getString("subtitle"))
                            )));
        });
    }

    public static void BotChatEvent(JSONObject json) {
        LoadConfig loadConfig = new LoadConfig();
        Map<String, Object> configServerList = loadConfig.getConfigServerList();
        BT.proxyServer.getAllPlayers().forEach(player -> {
            player.sendMessage(Component.text(json.getString("message")));
        });
    }
}
