package com.example.specgen.formatter;

import com.example.specgen.model.Field;

public class JavaFormatter{
	public String getJavaType(Field field){
		String type = field.getType().strip();
		String javaType = "";
		if (
				type.toLowerCase().equals("int") || type.toLowerCase().equals("char") || 
				type.toLowerCase().equals("character") || type.equals("String") ||
				type.toLowerCase().equals("integer")
			)
		{
			javaType = type;
		}
		else if (type.equals("uuid")){
			javaType = "Long";
		}
		else if (type.equals("long") || type.equals("boolean") || type.equals("Long") || type.equals("Boolean")){
			javaType = type;
		}
		else if (type.equals("string") ){
			javaType = "String";
		}
		
		return javaType;
		
	}
	public String toNonPrimitiveType(String type){
		type.strip();
		if(type.isBlank() || type.isEmpty()){
			return type;
		}
		String javaType = "";
		if (type.equals("int")){
			javaType = "Integer";
		}
		else if (type.equals("char")){
			javaType = "Character";
		}
		else if (type.equals("boolean")){
			javaType = "Boolean";
		}
		else if (type.equals("long")){
			javaType = "Long";
		}
		
		return javaType;
		
	}
}