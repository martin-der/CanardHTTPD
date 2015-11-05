package net.tetrakoopa.mdu.text.formater;


import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BufferedMustacheShaver<CONTEXT> implements MustacheShaver<CONTEXT> {

    private final static char ESCAPED_MUSTACHE_IN[] = escapeRegex(MUSTACHE_IN);
    private final static char ESCAPED_MUSTACHE_OUT[] = escapeRegex(MUSTACHE_OUT);

    public void process(Reader source, Writer destination, CONTEXT context, BarberTools<CONTEXT> tools) throws IOException {
        final StringBuffer sourceStringBuffer = new StringBuffer();
        final char buffer[] = new char[200];
        int l;
        while ((l=source.read(buffer))>0) {
            sourceStringBuffer.append(buffer, 0, l);
        }
        final String sourceString = sourceStringBuffer.toString();


        final Pattern pattern = Pattern.compile("(" + String.valueOf(ESCAPED_MUSTACHE_IN) + ".*" + String.valueOf(ESCAPED_MUSTACHE_OUT) + ")");
        final Matcher matcher = pattern.matcher(sourceString);
        int lastWriten = 0;
        while (matcher.find()) {
            destination.write(sourceString, lastWriten, matcher.regionStart());
            final String group = matcher.group(1);
            tools.shave(context, group.substring(2,group.length()-2), destination, 0);
            lastWriten = matcher.regionEnd();
        }

    }

    private static char[] escapeRegex(char string[]) {
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