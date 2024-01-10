package cn.esuny.bt.core.service;

import cn.esuny.bt.config.LoadConfig;
import cn.esuny.bt.init.handler.WebSocketHandler;
import cn.esuny.bt.util.websocket.client.WebSocketClient;
import cn.esuny.bt.util.websocket.handshake.ServerHandshake;
import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import cn.esuny.bt.BT;

import static java.lang.String.format;

public class WebSocketService {
    public static final Logger LOGGER = BT.logger;
    public static WebSocketClient client = null;
    private static final LoadConfig loadConfig = new LoadConfig();
    public static WebSocketHandler Handler = new WebSocketHandler();

    public static void main() {
        String url = loadConfig.getServer();
        try{
        client = new WebSocketClient(new URI(url)) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                LOGGER.info("�� ��a����Ϣ��⵽ws������ ��6�����T��");
                client.send(JSONObject.toJSONString(JSONObject.parseObject(format("{\"STATUS\":\"CHECKING\", \"name\":\"%s\", \"key\":\"%s\"}", loadConfig.getClient(), loadConfig.getUuid()))));
            }

            @Override
            public void onMessage(String message) {
                JSONObject json = JSONObject.parseObject(message.replace('\'', '"'));
                if (Objects.equals(json.getString("STATUS"), "OK")) {
                    LOGGER.info("�� ��aϲ�����ӳɹ� ��6�����T��");
                    return;
                }
                if (Objects.equals(json.getString("STATUS"), "ERROR")) {
                    throw new RuntimeException("Error To Connect BTBot");
                }
                if (Objects.equals(json.getString("STATUS"), "CHECKING")) {
                    client.send(JSONObject.toJSONString(JSONObject.parseObject(format("{\"STATUS\":\"CHECKING\", \"name\":\"%s\", \"key\":\"%s\"}", loadConfig.getClient(), loadConfig.getUuid()))));
                    return;
                }
                try {
                    Handler.getClass().getMethod(json.getString("Mode"), JSONObject.class).invoke(Handler.getClass().getConstructor(), json);
                } catch (NoSuchMethodException ignored) {
                } catch (IllegalAccessException | InvocationTargetException e) {
                    LOGGER.error(e.toString());
                    LOGGER.error("�� ��e�������� ��6�����T��");
                } catch (Exception e) {
                    LOGGER.error("�� ��eδ֪���� ��6�����T��");
                    LOGGER.error(e.getMessage());
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                try {
                    LOGGER.warn("�� ��cWS����ʧ��,����ws�������� ��6�����T��");
//                    Thread.sleep(120000);
//                    WebSocketService.client.connect();
                } catch (Exception e) {
                    BT.logger.error(e.toString());
                }
                BT.logger.error(reason);
            }

            @Override
            public void onError(Exception ex) {
                try {
                    LOGGER.warn("�� ��cWS����ʧ��,����ws�������� ��6�����T��");
//                    Thread.sleep(120000);
//                    WebSocketService.client.connect();
                } catch (Exception e) {
                    BT.logger.error(e.toString());
                }
                BT.logger.error(ex.toString());
            }
        };}
        catch (Exception exception){
            LOGGER.error(exception.toString());
        }
    }

    public static void send(String msg) {
        if (client != null && client.isOpen())
            client.send(msg);
    }

    public static void send_dict(String msg) {
        if (client != null && client.isOpen()) {
            JSONObject js = JSONObject.parseObject(msg);
            js.put("name", loadConfig.getClient());
            client.send(JSONObject.toJSONString(js));
        }
    }

}
