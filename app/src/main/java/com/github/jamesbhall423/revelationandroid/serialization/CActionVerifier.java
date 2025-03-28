package com.github.jamesbhall423.revelationandroid.serialization;
import java.lang.reflect.*;

import com.github.jamesbhall423.revelationandroid.action.ScanAction;
import com.github.jamesbhall423.revelationandroid.action.SquareAction;
import com.github.jamesbhall423.revelationandroid.model.CAction;

public class CActionVerifier {
    private static final String[] scanIndicies =  {"centerX","centerY"};
    private static final String[] squareIndicies =  {"x","y"};
    public static boolean verifyCAction(CAction in) {
        Class<?> checkClass = null;
        String[] useFields = null;
        if (in instanceof ScanAction) {
            checkClass = ScanAction.class;
            useFields = scanIndicies;
        } else if (in instanceof SquareAction) {
            checkClass = SquareAction.class;
            useFields = squareIndicies;
        }
        try {
            if (checkClass!=null) for (int i = 0; i < useFields.length; i++) {
                Field next = checkClass.getDeclaredField(useFields[i]);
                next.setAccessible(true);
                if (next.getInt(in)<0||next.getInt(in)>=8) return false;
                next.setAccessible(false);
            }
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
