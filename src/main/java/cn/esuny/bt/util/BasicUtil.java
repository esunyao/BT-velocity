package cn.esuny.bt.util;

public class BasicUtil {
    /**
     * char�����а���Ŀ���ַ�
     *
     * @param c Ŀ���ַ�
     * @param chars char����
     * @return �жϵĽ��
     */
    public static boolean matchAny(char c, char[] chars){
        for (char aChar : chars) {
            if (c == aChar) {
                return true;
            }
        }
        return false;
    }

    /**
     * String�����а���Ŀ���ַ���
     *
     * @param s Ŀ���ַ���
     * @param strs String����
     * @return �жϽ��
     */
    public static boolean matchAny(String s, String[] strs){
        for (String str : strs) {
            if (str.equals(s)) {
                return true;
            }
        }
        return false;
    }
}
