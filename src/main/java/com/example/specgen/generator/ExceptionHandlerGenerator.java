package com.example.specgen.generator;

import java.util.HashMap;
import java.util.Map;

import com.example.specgen.formatter.JavaFormatter;
import com.example.specgen.model.Entity;
import com.example.specgen.model.Field;

import freemarker.template.Configuration;

import com.example.specgen.writer.EntityWriter;
import com.example.specgen.writer.TemplateWriter;
public class ExceptionHandlerGenerator implements Generator{
	private final TemplateWriter writer;
    private Entity entity;
    private String content;

    public ExceptionHandlerGenerator(TemplateWriter writer) {
        this.writer = writer;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

	@Override
	public void generate() throws Exception {


		Map<String, Object> model = new HashMap<>();
		model.put("package", entity.getPackage());

        this.content = writer.render("exception-handler.ftl",model);

	}

	@Override
	public String getContent(){
		return content;
	}

	@Override
	public String getName(){
		return "GlobalExceptionHandler.java";
	}
	
}