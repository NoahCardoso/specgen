package com.example.specgen.generator;
import com.example.specgen.formatter.PostgresSqlFormatter;
import com.example.specgen.model.Entity;
import com.example.specgen.model.Field;
import com.example.specgen.writer.PostgresSqlWriter;

public class PostgresSqlGenerator implements Generator{
	
	private final Entity spec;
	private final String fileName = "schema.sql";

	public PostgresSqlGenerator(Entity spec){
		this.spec = spec;
	}

	@Override
	public void generate(){
		PostgresSqlWriter writer = new PostgresSqlWriter(new StringBuilder());
		PostgresSqlFormatter formatter = new PostgresSqlFormatter();
		writer.createTable(spec.getTable());
		for (String key: spec.getFields().keySet()){
			Field field = spec.getFields().get(key);

			writer.addField(key, formatter.getPostgressSqlType(field), formatter.getProstgressSqlProperty(field));
		}
		writer.closeTable();
		writer.toFile(fileName);
	}

}