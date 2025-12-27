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
		
		if (javaType.isEmpty()){
			//error
		}
		return javaType;
		
	}
}