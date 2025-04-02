package com.github.jamesbhall423.revelationandroid.serialization;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.jamesbhall423.revelationandroid.action.DeclareVictory;
import com.github.jamesbhall423.revelationandroid.action.DoTurn;
import com.github.jamesbhall423.revelationandroid.action.Exit;
import com.github.jamesbhall423.revelationandroid.action.ScanAction;
import com.github.jamesbhall423.revelationandroid.action.SquareAction;
import com.github.jamesbhall423.revelationandroid.action.StartNotification;
import com.github.jamesbhall423.revelationandroid.model.CAction;
import com.github.jamesbhall423.revelationandroid.model.CMap;
import com.github.jamesbhall423.revelationandroid.model.Player;
import com.github.jamesbhall423.revelationandroid.model.SquareState;

import java.lang.reflect.*;

public class JSONSerializer {
    private Map<String, EmptyConstructor> typeToClass;
    private Map<Class<?>, String> classToType;
    private String typeDescriptorName;
    private String newLine;
    private static final String INDENT_INCREASE = "    ";
    public static final JSONSerializer REVELATION_SERIALIZER = getRevelationSerializer();
    private static JSONSerializer getRevelationSerializer()  {
        String typeName = "object_type";
        Map<String, EmptyConstructor> typeConstructors = new HashMap<>();
        addTypeConstructor(typeConstructors, "DeclareVictory", DeclareVictory.class);
        addTypeConstructor(typeConstructors, "DoTurn", DoTurn.class);
        addTypeConstructor(typeConstructors, "Exit", Exit.class);
        typeConstructors.put("ScanAction", new EmptyConstructor() {
            @Override
            public Object create() {
                return new ScanAction(0, 0, 0, 0);
            }
            @Override
            public Class<?> getType() {
                return ScanAction.class;
            }
        });
        typeConstructors.put("SquareAction", new EmptyConstructor() {
            @Override
            public Object create() {
                return new SquareAction(0, 0, 0, false, false, 0);
            }
            @Override
            public Class<?> getType() {
                return SquareAction.class;
            }
        });
        typeConstructors.put("StartNotification", new EmptyConstructor() {
            @Override
            public Object create() {
                return new StartNotification(0, "");
            }
            @Override
            public Class<?> getType() {
                return StartNotification.class;
            }
        });
        typeConstructors.put("CMap", new EmptyConstructor() {
            @Override
            public Object create() {
                return new CMap(null,null);
            }
            @Override
            public Class<?> getType() {
                return CMap.class;
            }
        });
        typeConstructors.put("Player", new EmptyConstructor() {
            @Override
            public Object create() {
                return new Player(0);
            }
            @Override
            public Class<?> getType() {
                return Player.class;
            }
        });
        addTypeConstructor(typeConstructors, "CAction", CAction.class);
        addTypeConstructor(typeConstructors, "SquareState", SquareState.class);
        JSONSerializer serializer = new JSONSerializer(typeName, typeConstructors, CAction.LN);
        return serializer;
    }
    private static void addTypeConstructor(Map<String,EmptyConstructor> map, String type, final Class<?> clazz) {
        map.put(type,new EmptyConstructor() {

            @Override
            public Object create() {
                try {
                    Constructor<?> constructor = clazz.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    Object out = constructor.newInstance();
                    constructor.setAccessible(false);
                    return out;
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public Class<?> getType() {
                return clazz;
            }
        });
    }
    public JSONSerializer(String typeDescriptorName, Map<String, EmptyConstructor> typeConstructors, String newLine) {
        this.typeDescriptorName = typeDescriptorName;
        this.typeToClass = typeConstructors;
        classToType = new HashMap<>();
        Set<String> keySet = typeConstructors.keySet();
        this.newLine = newLine;
        for (String nextName: keySet) {
            if (classToType.containsValue(nextName)) throw new IllegalArgumentException("Duplicate typeName: " + nextName);
            classToType.put(typeConstructors.get(nextName).getType(),nextName);
        }
        validateNoDuplicateNames();
    }
    private void validateNoDuplicateNames() {
        for (Class<?> clazz: classToType.keySet()) {
            List<Field> fields = getFields(clazz);
            Set<String> references = new HashSet<>();
            references.add(typeDescriptorName);
            for (Field field: fields) {
                String name = field.getName();
                if (references.contains(name)) throw new IllegalArgumentException("Duplicate field name: "+name+" in Class "+clazz);
                references.add(name);
            }
        }
    }
    private List<Field> getFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (classToType.containsKey(clazz)) {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field next: declaredFields) if (!Modifier.isStatic(next.getModifiers())) fields.add(next);
            clazz = clazz.getSuperclass();
        }
        return fields;
    }
    private void appendLine(StringBuilder builder, String line, String indent) {
        builder.append(indent+line+newLine);
    }
    public String serializeObject(Object o) throws IllegalAccessException {
        StringBuilder out = new StringBuilder("");
        serializeObject(o,"",out);
        return out.toString();
    }
    public void serializeObject(Object o, String indent, StringBuilder out) throws IllegalAccessException {
        appendLine(out,"{","");
        Class<?> clazz = o.getClass();
        if (!classToType.containsKey(clazz)) throw new IllegalArgumentException("No specification for Class "+clazz+" value="+o);
        List<Field> fields = getFields(clazz);
        serializeField(typeDescriptorName, classToType.get(clazz), indent+INDENT_INCREASE, out);
        for (Field next: fields) {
            next.setAccessible(true);
            String name = next.getName();
            Object value = next.get(o);
            if (value!=null) {
                appendLine(out,",","");
                serializeField(name, value, indent+INDENT_INCREASE, out);
            }
            next.setAccessible(false);
        }
        out.append(newLine);
        out.append(indent+"}");
    }
    public void serializeField(String name, Object value, String indent, StringBuilder out) throws IllegalAccessException {
        out.append(indent+'\"'+name+"\": ");
        serializeValue(value, indent, out);
    }
    public void serializeValue(Object value, String indent, StringBuilder out) throws IllegalAccessException {
        Class<?> type = value.getClass();
        if (type.equals(String.class)) serializeString((String) value, out);
        else if (type.equals(Boolean.class)) out.append(value);
        else if (value instanceof Number) out.append(value);
        else if (type.isArray()) serializeArray(value, indent, out);
        else if (type.isEnum()) serializeEnum(value, indent, out);
        else serializeObject(value, indent, out);
    }
    public void serializeArray(Object array, String indent, StringBuilder out) throws IllegalAccessException {
        appendLine(out,"[","");
        for (int i = 0; i < Array.getLength(array); i++) {
            if (i>0) appendLine(out,",","");
            out.append(indent+INDENT_INCREASE);
            serializeValue(Array.get(array,i),indent+INDENT_INCREASE,out);
        }
        out.append(newLine+indent+"]");
    }
    public void serializeEnum(Object enumName, String indent, StringBuilder out) throws IllegalAccessException {
        out.append(enumName.toString());
    }
    public void serializeBoolean(Boolean object, StringBuilder out) {
        out.append(object.toString());
    }
    public void serializeDouble(Double object, StringBuilder out) {
        out.append(object.toString());
    }
    public void serializeString(String object, StringBuilder out) {
        out.append("\""+object+"\"");
    }
    public void serializeInteger(Integer object, StringBuilder out) {
        out.append(object.toString());
    }
    public Object deserializeObject(String in) throws IllegalArgumentException, IllegalAccessException {
        return deserializeObject(in,new AtomicInteger(0));
    }
    public Object deserializeObject(String in, AtomicInteger index) throws IllegalArgumentException, IllegalAccessException {
        JSONObjectJBH json = JSONObjectJBH.create(in,index);
        return fetchFromJson(json,null);
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private <T> Object fetchFromJson(JSONObjectJBH json, Class<T> type) throws IllegalArgumentException, IllegalAccessException {
        if (type==null || json instanceof ParsedObject) {
            ParsedObject parsed = (ParsedObject) json;
            StringObject typeO = (StringObject)parsed.get(typeDescriptorName);
            String typeName = typeO.value();
            Object out = typeToClass.get(typeName).create();
            Set<String> changedFields = parsed.keySet();
            List<Field> baseFields = getFields(out.getClass());
            for (Field next: baseFields) if (changedFields.contains(next.getName())) {
                next.setAccessible(true);
                next.set(out,fetchFromJson(parsed.get(next.getName()),next.getType()));
                next.setAccessible(false);
            }
            return out;
        } else if (json instanceof ParsedArray) {
            ParsedArray parsed = (ParsedArray) json;
            int length = parsed.length();
            Class<?> arrayType = type.getComponentType();
            Object out = Array.newInstance(arrayType,length);
            for (int i = 0; i < length; i++) {
                Object toSet = fetchFromJson(parsed.get(i), arrayType);
                Array.set(out,i,toSet);
            }
            return out;
        } else if (json instanceof StringObject) {
            return ((StringObject)json).value();
        }
        else if (json instanceof PrimitiveObject) {
            PrimitiveObject object = (PrimitiveObject) json;
            if (type.isEnum())  {
                return Enum.valueOf((Class<Enum>) type, object.value());
            } else if (type == Integer.TYPE) return Integer.valueOf(object.value());
            else if (type == Double.TYPE) return Double.valueOf(object.value());
            else if (type == Long.TYPE) return Long.valueOf(object.value());
            else if (type == Boolean.TYPE) return Boolean.valueOf(object.value());
            else if (type == Float.TYPE) return Float.valueOf(object.value());
            else if (type == Short.TYPE) return Short.valueOf(object.value());
            else if (type == Byte.TYPE) return Byte.valueOf(object.value());
            else throw new TypeNotPresentException(type.toString(),null);
        } else throw new TypeNotPresentException(json.toString(), null);
    }
    public CAction deserializeCAction(String json) throws IllegalAccessException {
        CAction out = (CAction) deserializeObject(json);
        if (!CActionVerifier.verifyCAction(out)) throw new IllegalArgumentException("invalid CAction");
        return out;
    }
    public CMap deserializeCMap(String json) throws IllegalAccessException {
        CMap out = (CMap) deserializeObject(json);
        if (!CMapVerifier.verifyCMap(out)) throw new IllegalArgumentException("invalid CMap");
        return out;
    }
    public String deserializeString(String json) {
        return json.substring(1,json.length()-1);
    }
}
