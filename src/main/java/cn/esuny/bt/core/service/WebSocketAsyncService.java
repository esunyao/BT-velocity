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
        BT.logger.info("§aBTWebsocket保护进程已开启,每" + loadConfig.getWait() + "s检查一次连接");
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
//                    BT.logger.error("§cBTWebsocket已关闭");
//                    WebSocketService.client.close();
                    continue;
                }
                if (!WebSocketService.client.isOpen()) {
                    WebSocketService.main();
//                    BT.logger.info("哦你干嘛"+String.valueOf(WebSocketService.client.isOpen()));
                    WebSocketService.client.connect();
//                    BT.logger.info("坤坤永不塌方"+String.valueOf(WebSocketService.client.isOpen()));
                }
            } catch (Exception exception) {
                BT.logger.error("§cBTWebsocket启动失败，将在" + loadConfig.getWait() + "s后重连");
            }
            if (WebSocketService.client.isOpen()) {
//                BT.logger.info("heartbeat"+String.valueOf(WebSocketService.client.isOpen()));
                WebSocketService.send("heartbeat");
            }

        }
    }
}
