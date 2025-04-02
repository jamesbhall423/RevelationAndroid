package com.github.jamesbhall423.revelationandroid.io;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public class TextOutputStream {
    private Writer writer;
    private boolean extraSpacing;
    public TextOutputStream(OutputStream stream, boolean extraSpacing) {
        writer = new OutputStreamWriter(stream);
        this.extraSpacing = extraSpacing;
    }

    public void write(String val) throws IOException {
        if (extraSpacing) writer.write(Character.toChars(TextConstants.START_TEXT));
        writer.write(val);
        if (extraSpacing) writer.write(Character.toChars(TextConstants.END_TEXT));
        writer.flush();
    }
    public void close() throws IOException {
        writer.close();
    }
}
