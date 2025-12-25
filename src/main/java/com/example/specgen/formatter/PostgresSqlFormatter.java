package com.example.specgen.formatter;

import com.example.specgen.model.Field;
public class PostgresSqlFormatter {


	public String getPostgressSqlType(Field field){
		String type = field.getType().strip().toLowerCase();
		System.out.println("TYPE is "+type);
		String pgsqlType = "";

		//string = TEXT, uuid = UUID, BOOLEAN
		if (type.equals("string") || type.equals("uuid") || type.equals("boolean")){
			pgsqlType = type.toUpperCase();
		}
		// int or integer = INTEGER
		else if (type.equals("int") || type.equals("integer")) {
			pgsqlType = "INTEGER";
		}
		//BIGINT
		//SERIAL
		//BIGSERIAL
		//TIME?

		if(pgsqlType.isEmpty()){
			//error
		}
		return pgsqlType;

	}

	public String getProstgressSqlProperty(Field field){
		String pgsqlProperty = "";
		if (field.isPrimary()){
			pgsqlProperty = "PRIMARY KEY";
		}
		else if (field.isUnique()){
			pgsqlProperty = "UNIQUE";
		}
		else if (!field.isNullable()){
			pgsqlProperty = "NOT NULL";
		}

		if (pgsqlProperty.isEmpty()){
			//error
		}
		return pgsqlProperty;
	}
}
