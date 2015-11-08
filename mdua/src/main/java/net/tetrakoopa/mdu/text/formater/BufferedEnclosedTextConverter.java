package net.tetrakoopa.mdu.text.formater;


import net.tetrakoopa.mdu.util.FileUtil;
import net.tetrakoopa.mdu.util.StringUtil;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BufferedEnclosedTextConverter<CONTEXT> implements EnclosedTextConverter<CONTEXT> {

    private final static char ENCLOSING_START[] = StringUtil.escapeRegex(MUSTACHE_IN);
    private final static char ENCLOSING_STOP[] = StringUtil.escapeRegex(MUSTACHE_OUT);

    //private final static Pattern pattern = Pattern.compile("(" + String.valueOf(ENCLOSING_START) + ".*" + String.valueOf(ENCLOSING_STOP) + ")");
    private final static Pattern pattern = Pattern.compile("(" + String.valueOf(ENCLOSING_START) + "[^\\}]*" + String.valueOf(ENCLOSING_STOP) + ")");

    public void process(Reader source, Writer destination, CONTEXT context, ConverterTools<CONTEXT> tools) throws IOException {
        final String sourceString = FileUtil.getAsString(source);
        final int sourceLen = sourceString.length();

        final Matcher matcher = pattern.matcher(sourceString);
        int lastWriten = 0;
        while (matcher.find()) {
            final int start = matcher.start(1);
            if (lastWriten<start) {
                destination.append(sourceString.substring(lastWriten, start));
            }
            final String group = matcher.group(1);
            tools.convert(context, group.substring(2, group.length() - 2), destination, 0);
            lastWriten = matcher.end(1);
        }
        if (lastWriten<sourceLen) {
            destination.append(sourceString.substring(lastWriten, sourceLen));
        }

    }

}