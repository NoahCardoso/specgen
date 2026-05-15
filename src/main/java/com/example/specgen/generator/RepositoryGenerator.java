package com.example.specgen.generator;
import java.util.HashMap;
import java.util.Map;

import com.example.specgen.formatter.JavaFormatter;
import com.example.specgen.model.Entity;
import com.example.specgen.model.Field;
import com.example.specgen.writer.RepositoryWriter;
import com.example.specgen.writer.TemplateWriter;

import freemarker.template.Configuration;
public class RepositoryGenerator implements Generator{
	private final TemplateWriter writer;
    private final JavaFormatter formatter;
    private Entity entity;
    private String content;

    public RepositoryGenerator(TemplateWriter writer, JavaFormatter formatter, Entity entity) {
        this.writer = writer;
        this.formatter = formatter;
		this.entity = entity;
    }

	public RepositoryGenerator(TemplateWriter writer, JavaFormatter formatter) {
        this.writer = writer;
        this.formatter = formatter;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }


	@Override
	public void generate() throws Exception{

        Map<String, Object> model = new HashMap<>();
		model.put("package", entity.getPackage());
        model.put("entity", entity.getName());
		model.put("table", entity.getTable());
		model.put("primaryKey", entity.getPrimaryKey());
		model.put("primaryKeyType", formatter.toNonPrimitiveType(formatter.getJavaType(entity.getFields().get(entity.getPrimaryKey()))));
		
		this.content = writer.render("repository.ftl",model);
        
		
	}
	@Override
	public String getContent(){
		return content;
	}

	@Override
	public String getName(){
		return entity.getName()+"Repository.java";
	}
	
}