package edu.geekhub.homework.util;


import java.lang.reflect.Type;

public class TypeParser {

    private TypeParser() {
    }

    public static Object parseByType(Type type, String value) {
        if (type.equals(String.class)) {
            return value;
        }
        if (type.equals(Integer.TYPE) || type.equals(Integer.class)) {
            return Integer.parseInt(value);
        }
        if (type.equals(Short.TYPE) || type.equals(Short.class)) {
            return Short.parseShort(value);
        }
        if (type.equals(Long.TYPE) || type.equals(Long.class)) {
            return Long.parseLong(value);
        }
        if (type.equals(Character.TYPE) || type.equals(Character.class)) {
            return value.charAt(0);
        }
        if (type.equals(Float.TYPE) || type.equals(Float.class)) {
            return Float.parseFloat(value);
        }
        if (type.equals(Double.TYPE) || type.equals(Double.class)) {
            return Double.parseDouble(value);
        }
        if (type.equals(Boolean.TYPE) || type.equals(Boolean.class)) {
            return Boolean.parseBoolean(value);
        }
        throw new IllegalArgumentException("Can't parse type: " + type);
    }
}
