package cn.esuny.bt.core.service;

import cn.esuny.bt.BT;
import cn.esuny.bt.config.LoadConfig;

public class WebSocketAsyncService extends Thread {
    private static final LoadConfig loadConfig = new LoadConfig();
    public static boolean status = true;

    public void setStatus(boolean status) {
        WebSocketAsyncService.status = status;
    }

    public boolean getStatus() {
        return status;
    }

    @Override
    public void run() {
        BT.logger.info("��aBTWebsocket���������ѿ���,ÿ" + loadConfig.getWait() + "s���һ������");
        WebSocketService.main();
        WebSocketService.client.connect();
//        BT.logger.info("start"+String.valueOf(WebSocketService.client.isOpen()));
        while (true) {
//            BT.logger.info("before"+String.valueOf(WebSocketService.client.isOpen()));
            try {
                Thread.sleep(loadConfig.getWait() * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                if(!status){
//                    BT.logger.error("��cBTWebsocket�ѹر�");
//                    WebSocketService.client.close();
                    continue;
                }
                if (!WebSocketService.client.isOpen()) {
                    WebSocketService.main();
//                    BT.logger.info("Ŷ�����"+String.valueOf(WebSocketService.client.isOpen()));
                    WebSocketService.client.connect();
//                    BT.logger.info("������������"+String.valueOf(WebSocketService.client.isOpen()));
                }
            } catch (Exception exception) {
                BT.logger.error("��cBTWebsocket����ʧ�ܣ�����" + loadConfig.getWait() + "s������");
            }
            if (WebSocketService.client.isOpen()) {
//                BT.logger.info("heartbeat"+String.valueOf(WebSocketService.client.isOpen()));
                WebSocketService.send("heartbeat");
            }

        }
    }
}
