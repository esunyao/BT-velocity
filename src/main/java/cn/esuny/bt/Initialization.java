package cn.esuny.bt;

import org.slf4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class Initialization {
    // 配置文件目录
    private static final File CONFIG_FILE = new File("./plugins/BTAPI/config.toml");
    // 配置文件夹
    private static final File CONFIG_FOLDER = new File("./plugins/BTAPI/");
    // 读取 resource 目录下的配置文件
    private static final InputStream CONFIG_RESOURCE = Initialization.class.getClassLoader().getResourceAsStream("config.toml");

    /**
     * 初始化插件
     */
    public static void init(Logger logger) {
        logger.info(BT.class.getClassLoader().getResource("resources/config.toml").toString());
        try {
            byte[] data = new byte[1024 * 10];
            int len;
            // 如果配置文件不存在
            if (!CONFIG_FILE.exists()) {
                // 创建配置文件夹
                CONFIG_FOLDER.mkdirs();
                // 创建配置文件
                RandomAccessFile raf = new RandomAccessFile(CONFIG_FILE, "rw");
                // 读取 resource 目录下的配置文件
                while ((len = CONFIG_RESOURCE.read(data)) != 1) {
                    // 写入配置文件
                    raf.write(data, 0, len);
                }
                // 关闭流
                CONFIG_RESOURCE.close();
                raf.close();
            }
        } catch (Exception e) {

        }

    }
}
