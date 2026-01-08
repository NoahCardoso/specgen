package com.example.specgen.validator;

import com.example.specgen.formatter.JavaFormatter;
import com.example.specgen.formatter.PostgreSqlFormatter;
import com.example.specgen.model.Entity;

public class SpecValidator{
	
	private Entity spec;

	public SpecValidator(Entity spec){
		this.spec = spec;
	}

	public boolean check() throws Exception{
		try {
			if(!(spec.getName().matches("[A-Z]+[a-z0-9]*"))){
				throw new Exception("Invalid entity name");
			}
		} catch (Exception e) {
			throw new Exception("Entity field mush have a value");
		}
		
		try {
			if(!(spec.getTable().matches("[A-Za-z]+[A-Za-z0-9]*_*[A-Za-z0-9]*"))){
				throw new Exception("Invalid table name");
			}
		} catch (Exception e) {
			throw new Exception("Table field mush have a value");
		}


		int primaryCount = 0;

		for (String key: spec.getFields().keySet()){
			try {
				if(!(key.matches("[A-Za-z]+[A-Za-z0-9]*"))){
					throw new Exception("Invalid field name");
				}
				
			} catch (Exception e) {
				throw new Exception("Invalid field name");
			}
			try {
				if(!(spec.getFields().get(key).getType().matches("[A-Za-z]+[A-Za-z]*"))){
					throw new Exception("Invalid field type");
				}
			} catch (Exception e) {
				throw new Exception("Field type required");
			}

			//ensures that type is supported by the formatter's
			
			JavaFormatter jf = new JavaFormatter();
			String type = jf.getJavaType(spec.getFields().get(key));
			if(type.isBlank()){
				throw new Exception("Unsupported type in java");
			}

			PostgreSqlFormatter pf = new PostgreSqlFormatter();
			type = pf.getPostgreSqlType(spec.getFields().get(key));
			if(type.isBlank()){
				throw new Exception("Unsupported type in PgSql");
			}

			if (spec.getFields().get(key).isPrimary()){
					primaryCount++;
			}
			
		}

		if (primaryCount != 1) {
			throw new Exception("Must have only one Primary Key");
		}

		return true;
	}
}