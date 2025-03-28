package com.github.jamesbhall423.revelationandroid.serialization;
import java.util.concurrent.atomic.AtomicInteger;

public class StringObject extends JSONObjectJBH {
    private String value;
    public StringObject(String in, AtomicInteger index) {
        int startIndex = in.indexOf("\"",index.get())+1;
        int escapeIndex = in.indexOf("\\",index.get());
        int endIndex = in.indexOf("\"",startIndex);
        while (escapeIndex>0&&escapeIndex<endIndex) {
            if (escapeIndex==endIndex-1) {
                escapeIndex = in.indexOf("\\",endIndex+1);
                endIndex = in.indexOf("\"",endIndex+1);
            } else {
                escapeIndex = in.indexOf("\\",escapeIndex+2);
            }
        }
        value = in.substring(startIndex, endIndex);
        index.set(endIndex+1);
    }
    public String value() {
        return value;
    }
}
