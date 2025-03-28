package com.github.jamesbhall423.revelationandroid.io;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class TextInputStream {
    
    private Reader reader;
    public TextInputStream(InputStream stream) {
        reader = new InputStreamReader(stream);
    }
    public String read() throws IOException {
        StringBuilder out = new StringBuilder();
        int read = reader.read();
        while (read!=TextConstants.END_TEXT) {
            if (read!=TextConstants.START_TEXT) out.append(Character.toChars(read));
            read = reader.read();
        }
        return out.toString();
    }
    public void close() throws IOException {
        reader.close();
    }
}
