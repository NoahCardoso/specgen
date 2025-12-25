package com.example.specgen.generator;
import com.example.specgen.formatter.PostgresSqlFormatter;
import com.example.specgen.model.Entity;
import com.example.specgen.model.Field;
import com.example.specgen.writer.PostgresSqlWriter;
public class PostgresSqlGenerator{
	
	private final Entity spec;
	public PostgresSqlGenerator(Entity spec){
		this.spec = spec;
	}

	public String generate(){
		PostgresSqlWriter writer = new PostgresSqlWriter(new StringBuilder());
		PostgresSqlFormatter formatter = new PostgresSqlFormatter();
		writer.createTable(spec.getTable());
		for (String key: spec.getFields().keySet()){
			Field field = spec.getFields().get(key);

			writer.addField(key, formatter.getPostgressSqlType(field), formatter.getProstgressSqlProperty(field));
		}
		writer.closeTable();
		return writer.getStringFile();
	}

}