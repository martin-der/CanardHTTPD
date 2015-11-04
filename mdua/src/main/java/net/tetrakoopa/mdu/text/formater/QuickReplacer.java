package net.tetrakoopa.mdu.text.formater;

import android.util.Log;

import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuickReplacer<CONTEXT> {

    private final static char MUSTACHE_IN[] = "{{".toCharArray();
    private final static char MUSTACHE_OUT[] = "}}".toCharArray();

    public interface BarberTools<CONTEXT> {

        void shave(CONTEXT context, String key, Writer destination, int extraMustaches) throws IOException;

        Object comment(CONTEXT context, String subject);
    }

    public void process(Reader source, Writer destination, CONTEXT context, BarberTools<CONTEXT> tools) throws IOException {
        final StringBuffer sourceStringBuffer = new StringBuffer();
        final char buffer[] = new char[200];
        int l;
        while ((l=source.read(buffer))>0) {
            sourceStringBuffer.append(buffer, 0, l);
        }
        final String sourceString = sourceStringBuffer.toString();


        //final Pattern pattern = Pattern.compile("(" + String.valueOf(MUSTACHE_IN) + ".*" + String.valueOf(MUSTACHE_OUT) + ")");
        final Pattern pattern = Pattern.compile("(" + "\\{\\{" + ".*" + "\\}\\}" + ")");
        final Matcher matcher = pattern.matcher(sourceString);
        int lastWriten = 0;
        while (matcher.find()) {
            destination.write(sourceString, lastWriten, matcher.regionStart());
            final String group = matcher.group(1);
            tools.shave(context, group.substring(2,group.length()-4), destination, 0);
            lastWriten = matcher.regionEnd();
        }

    }
}