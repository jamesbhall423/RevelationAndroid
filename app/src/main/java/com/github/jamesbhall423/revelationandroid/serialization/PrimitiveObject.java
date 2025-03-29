package com.github.jamesbhall423.revelationandroid.serialization;
import java.util.concurrent.atomic.AtomicInteger;

public class PrimitiveObject extends JSONObjectJBH {
    private String value;
    public PrimitiveObject(String in, AtomicInteger index){
        int commaIndex = in.indexOf(",",index.get())+1;
        int bracketIndex = in.indexOf("]",index.get());
        int braceIndex = in.indexOf("}",index.get());
        int finalIndex = braceIndex;
        if (commaIndex>0&&commaIndex<finalIndex) finalIndex = commaIndex;
        if (bracketIndex>=0&&bracketIndex<finalIndex) finalIndex = bracketIndex;
        String useString = in.substring(index.get(), finalIndex).trim();
        if (in.charAt(finalIndex-1) == ',') useString = useString.substring(0, useString.length()-1);
        value = useString;
        index.set(finalIndex);
    }
    public String value() {
        return value;
    }
    
}
