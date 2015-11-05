package net.tetrakoopa.mdu.util;

public class StringUtil {

    public static char[] escapeRegex(char string[]) {
        final StringBuffer escaped = new StringBuffer();
        final int len = string.length;
        for (int i =0; i<len; i++) {
            final char c = string[i];
            if (".*?()[]{}^S|+".contains(String.valueOf(c))) {
                escaped.append('\\');
            }
            if (c=='\\' && i<len-1) {
                i++;
                continue;
            }
            escaped.append(c);
        }
        return escaped.toString().toCharArray();
    }

}
