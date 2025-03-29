package com.github.jamesbhall423.revelationandroid.serialization;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ParsedObject extends JSONObjectJBH {
    private Map<String, JSONObjectJBH> fields = new HashMap<>();
    public ParsedObject(String in, AtomicInteger index) {
        int colon = in.indexOf(":",index.get());
        int end = in.indexOf("}",index.get());
        while (colon>=0&&colon<end) {
            String key = in.substring(in.indexOf("\"",index.get())+1,colon-1);
            index.set(colon+1);
            JSONObjectJBH value = JSONObjectJBH.create(in,index);
            if (fields.containsKey(key)) throw new RuntimeException("key "+ key+" already exists in object");
            fields.put(key,value);
            colon = in.indexOf(":",index.get());
            end = in.indexOf("}",index.get());
        }

        index.set(end+1);
    }
    public JSONObjectJBH get(String key) {
        return fields.get(key);
    }
    public Set<String> keySet() {
        return fields.keySet();
    }
}
