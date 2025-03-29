package com.github.jamesbhall423.revelationandroid.serialization;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ParsedArray extends JSONObjectJBH {
    private List<JSONObjectJBH> objects = new ArrayList<>();
    public ParsedArray(String in, AtomicInteger index) {
        int end = in.indexOf("]",index.get());
        index.set(in.indexOf("[",index.get())+1);
        int prior = index.get();
        int first = index.get();
        while (index.get()<end&&!in.substring(index.get(),end).trim().isEmpty()) {
            objects.add(JSONObjectJBH.create(in,index));
            if (prior >= index.get()) {
                System.out.println(prior);
                System.out.println(index.get());
                System.out.println(in.substring(first,end));
                System.out.println(in.substring(index.get(),end));
                throw new RuntimeException();
            }
            prior=index.get();
            end = in.indexOf("]",index.get());
        }
        index.set(end+1);
    }

    public int length() {
        return objects.size();
    }
    public JSONObjectJBH get(int index) {
        return objects.get(index);
    }
}
