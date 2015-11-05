package net.tetrakoopa.mdu.text.formater;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public interface MustacheShaver<CONTEXT> {

    char MUSTACHE_IN[] = "{{".toCharArray();
    char MUSTACHE_OUT[] = "}}".toCharArray();


    interface BarberTools<CONTEXT> {

        void shave(CONTEXT context, String key, Writer outputStream, int extraMustaches) throws IOException;

        Object comment(CONTEXT context, String subject);
    }

    void process(Reader source, Writer destination, CONTEXT context, BarberTools<CONTEXT> tools) throws IOException;
}