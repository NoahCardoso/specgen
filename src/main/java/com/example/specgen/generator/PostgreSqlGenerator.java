package com.example.specgen.generator;
import com.example.specgen.formatter.PostgreSqlFormatter;
import com.example.specgen.model.Entity;
import com.example.specgen.model.Field;
import com.example.specgen.writer.PostgreSqlWriter;

public class PostgreSqlGenerator implements Generator{
	
	private final Entity spec;
	private final String filename = "schema.sql";
	private String content;
	
	public PostgreSqlGenerator(Entity spec){
		this.spec = spec;
	}

	@Override
	public void generate(){
		PostgreSqlWriter writer = new PostgreSqlWriter(new StringBuilder());
		PostgreSqlFormatter formatter = new PostgreSqlFormatter();
		writer.createTable(spec.getTable());
		for (String key: spec.getFields().keySet()){
			Field field = spec.getFields().get(key);

			writer.addField(key, formatter.getPostgreSqlType(field), formatter.getProstgreSqlProperty(field));
		}
		writer.closeTable();
		this.content = writer.getStringFile();
	}

	@Override
	public String getContent(){
		return content;
	}

	@Override
	public String getName(){
		return filename;
	}

}