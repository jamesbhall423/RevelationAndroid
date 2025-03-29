package com.github.jamesbhall423.revelationandroid.io;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public class TextOutputStream {
    private Writer writer;
    public TextOutputStream(OutputStream stream) {
        writer = new OutputStreamWriter(stream);
    }

    public void write(String val) throws IOException {
        writer.write(Character.toChars(TextConstants.START_TEXT));
        writer.write(val);
        writer.write(Character.toChars(TextConstants.END_TEXT));
        writer.flush();
    }
    public void close() throws IOException {
        writer.close();
    }
}
