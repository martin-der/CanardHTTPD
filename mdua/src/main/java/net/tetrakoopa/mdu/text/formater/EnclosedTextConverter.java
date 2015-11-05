package net.tetrakoopa.mdu.text.formater;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public interface EnclosedTextConverter<CONTEXT> {

    char MUSTACHE_IN[] = "{{".toCharArray();
    char MUSTACHE_OUT[] = "}}".toCharArray();


    interface ConverterTools<CONTEXT> {

        void convert(CONTEXT context, String key, Writer outputStream, int extraMustaches) throws IOException;

        Object comment(CONTEXT context, String subject);
    }

    void process(Reader source, Writer destination, CONTEXT context, ConverterTools<CONTEXT> tools) throws IOException;
}