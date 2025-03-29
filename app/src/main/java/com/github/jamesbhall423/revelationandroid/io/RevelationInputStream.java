package com.github.jamesbhall423.revelationandroid.io;
import java.io.IOException;
import java.io.InputStream;

import com.github.jamesbhall423.revelationandroid.model.CAction;
import com.github.jamesbhall423.revelationandroid.model.CMap;
import com.github.jamesbhall423.revelationandroid.serialization.JSONSerializer;

public class RevelationInputStream {
    private TextInputStream stream;
    private JSONSerializer serializer;
    public RevelationInputStream(InputStream stream, JSONSerializer serializer) {
        this.stream = new TextInputStream(stream);
        this.serializer = serializer;
    }
    public CAction readCAction() throws IllegalAccessException, IOException {
        return serializer.deserializeCAction(stream.read());
    }
    public CMap readCMap() throws IllegalAccessException, IOException {
        return serializer.deserializeCMap(stream.read());
    }
    public int readInt() throws IOException {
        return Integer.parseInt(stream.read());
    }
    public void close() throws IOException {
        stream.close();
    }
}
