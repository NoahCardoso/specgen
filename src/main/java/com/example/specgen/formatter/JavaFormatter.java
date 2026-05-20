package com.example.specgen.formatter;

import org.springframework.stereotype.Component;

import com.example.specgen.exception.UnsupportedTypeException;
import com.example.specgen.model.Field;

@Component
public class JavaFormatter {

    public String getJavaType(Field field) {
        String type = field.getType().strip().toLowerCase();

        return switch (type) {
            case "string"    -> "String";
            case "int",
                 "integer"   -> "int";
            case "long"      -> "long";
            case "boolean",
                 "bool"      -> "boolean";
            case "char",
                 "character" -> "char";
            case "double"    -> "double";
            case "float"     -> "float";
            case "uuid"      -> "UUID";
            case "relation"  -> "relation";
            default -> throw new UnsupportedTypeException(
                    "Unsupported field type: '" + field.getType().strip() + "'"
            );
        };
    }

    public String toNonPrimitiveType(String type) {
        type = type.strip(); 

        if (type.isBlank()) { 
            return type;
        }

        return switch (type) {
            case "int"     -> "Integer";
            case "char"    -> "Character";
            case "boolean" -> "Boolean";
            case "long"    -> "Long";
            default -> throw new UnsupportedTypeException(
                    "No boxed type for: '" + type + "'"
            );
        };
    }
}