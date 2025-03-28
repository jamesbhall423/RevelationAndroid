package com.github.jamesbhall423.revelationandroid.serialization;
import java.util.concurrent.atomic.AtomicInteger;

public class JSONObjectJBH {
    public static JSONObjectJBH create(String in, AtomicInteger index) {
        JSONObjectJBH out = null;
        for (int i = 0; out==null; i++) {
            char c = in.charAt(index.get()+i);
            if (c=='\"') out = new StringObject(in,index);
            else if (c=='}'||c==']') out = new PrimitiveObject(in,index);
            else if (c==',') {
                out = new PrimitiveObject(in,index);
            }
            else if (c=='{') out = new ParsedObject(in,index);
            else if (c=='[') out = new ParsedArray(in,index);
        }
        if (index.get()<in.length()&&in.charAt(index.get())==',') index.getAndIncrement();
        return out;
    }
}
