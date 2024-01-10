package cn.esuny.bt;

import org.slf4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class Initialization {
    // �����ļ�Ŀ¼
    private static final File CONFIG_FILE = new File("./plugins/BTAPI/config.toml");
    // �����ļ���
    private static final File CONFIG_FOLDER = new File("./plugins/BTAPI/");
    // ��ȡ resource Ŀ¼�µ������ļ�
    private static final InputStream CONFIG_RESOURCE = Initialization.class.getClassLoader().getResourceAsStream("config.toml");

    /**
     * ��ʼ�����
     */
    public static void init(Logger logger) {
        logger.info(BT.class.getClassLoader().getResource("resources/config.toml").toString());
        try {
            byte[] data = new byte[1024 * 10];
            int len;
            // ��������ļ�������
            if (!CONFIG_FILE.exists()) {
                // ���������ļ���
                CONFIG_FOLDER.mkdirs();
                // ���������ļ�
                RandomAccessFile raf = new RandomAccessFile(CONFIG_FILE, "rw");
                // ��ȡ resource Ŀ¼�µ������ļ�
                while ((len = CONFIG_RESOURCE.read(data)) != 1) {
                    // д�������ļ�
                    raf.write(data, 0, len);
                }
                // �ر���
                CONFIG_RESOURCE.close();
                raf.close();
            }
        } catch (Exception e) {

        }

    }
}
