package cn.esuny.bt.util;

public class MinecraftColorCodeUtil {
    private static final char[] colorCode = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'k', 'l', 'm', 'n', 'o', 'r'};
    public static String replaceColorCode(String message) {
        StringBuilder builder = new StringBuilder();
        // ����Ϣת��Ϊchar����
        char[] messageChars = message.toCharArray();
        // �����ж�char����������һλ֮����ַ��ǲ��ǡ�&��
        for (int i = 0, length = messageChars.length - 1; i < length; i++) {
            // ���char�����е�һ���ַ�����&������һ���ַ�������ɫ�����б�
            if (messageChars[i] == '&' && BasicUtil.matchAny(messageChars[i + 1], colorCode)){
                // ���ҵ�������ɫ�ַ���ӵ��ַ�����
                builder.append('��');
            } else {
                // �����ַ�׷��
                builder.append(messageChars[i]);
            }
        }
        // ׷�����һ���ַ�
        builder.append(messageChars[messageChars.length - 1]);
        return builder.toString();
    }
}
