package com.mds.myysb.utils;

public class StringUtil {

    /**
     * 判断字符串是否为空串
     * @param string 需要判断的字符串
     * @return true 为空串, false 不为空
     */
    public static boolean isStrNull(String string) {
        if (string == null || "null".equals(string) || "NULL".equals(string)) {
            return true;
        }
        String str = string.replace(" ", "");
        return str.length() == 0;
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、 换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 字符串截取方法
     */
    public static String subStringForName(String arg, int subLen) {
        StringBuffer sb = new StringBuffer();
        int len = 0;
        String item = null;
        for (int i = 0; i < arg.length() && len < subLen; i++) {
            item = arg.substring(i, i + 1);
            sb.append(item);
            if (java.util.regex.Pattern.matches("([/d/D]*)", item)) {
                len = len + 2;
            } else {
                len++;
            }
        }
        return sb.toString() + (arg.length() > subLen ? "..." : "");
    }

    public static String formatJsonStr(String string) {
        return formatJsonStr(string, "{", "}");
    }

    public static String formatJsonStr(String string, String begin, String end) {
        if (isStrNull(string)) {
            return "";
        }
        int b = string.indexOf(begin);
        int e = string.lastIndexOf(end);
        //  当String 不包含'{' 或者 '}' 返回它本事身
        if (b == -1 || e == -1) {
            return string;
        }
        return string.substring(b, e + 1);
    }

}
