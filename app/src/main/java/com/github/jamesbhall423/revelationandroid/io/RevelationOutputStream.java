package com.github.jamesbhall423.revelationandroid.io;
import java.io.IOException;
import java.io.OutputStream;

import com.github.jamesbhall423.revelationandroid.model.CAction;
import com.github.jamesbhall423.revelationandroid.model.CMap;
import com.github.jamesbhall423.revelationandroid.serialization.JSONSerializer;

public class RevelationOutputStream {
    private TextOutputStream stream;
    private JSONSerializer serializer;
    public RevelationOutputStream(OutputStream stream, JSONSerializer serializer, boolean extraSpacing) {
        this.stream = new TextOutputStream(stream,extraSpacing);
        this.serializer = serializer;
    }
    public void writeCAction(CAction action) throws IOException, IllegalAccessException {
        stream.write(serializer.serializeObject(action));
    }
    public void writeCMap(CMap cmap) throws IOException, IllegalAccessException {
        stream.write(serializer.serializeObject(cmap));
    }
    public void writeInt(int val) throws IOException {
        stream.write(val+"");
    }
    public void writeString(String val) throws IOException {
        stream.write(val);
    }
    public void close() throws IOException {
        stream.close();
    }
}