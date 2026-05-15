package com.example.specgen.generator;
import java.util.HashMap;
import java.util.Map;

import com.example.specgen.formatter.JavaFormatter;
import com.example.specgen.model.Entity;
import com.example.specgen.model.Field;
import com.example.specgen.writer.ControllerWriter;
import com.example.specgen.writer.TemplateWriter;

import freemarker.template.Configuration;

public class ServiceGenerator implements Generator{
	private final TemplateWriter writer;
    private final JavaFormatter formatter;
    private Entity entity;
    private String content;

    public ServiceGenerator(TemplateWriter writer, JavaFormatter formatter, Entity entity) {
        this.writer = writer;
        this.formatter = formatter;
		this.entity = entity;
    }

	public ServiceGenerator(TemplateWriter writer, JavaFormatter formatter) {
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
		model.put("fields", entity.getFields());
		model.put("primaryKey", entity.getPrimaryKey());
		model.put("primaryKeyType", entity.getFields().get(entity.getPrimaryKey()).getType());

		this.content = writer.render("service.ftl",model);
	}

	@Override
	public String getContent(){
		return content;
	}

	@Override
	public String getName(){
		return entity.getName() + "Service.java";
	}
	
}