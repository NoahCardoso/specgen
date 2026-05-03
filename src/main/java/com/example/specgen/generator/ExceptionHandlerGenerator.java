package com.example.specgen.generator;

import com.example.specgen.formatter.JavaFormatter;
import com.example.specgen.model.Entity;
import com.example.specgen.model.Field;

import freemarker.template.Configuration;

import com.example.specgen.writer.EntityWriter;
public class ExceptionHandlerGenerator implements Generator{
	private final Entity spec;
	private String filename = "";
	private String content = "";
	public ExceptionHandlerGenerator(Entity spec){
		this.spec = spec;
	}

	@Override
	public void generate() throws Exception {

		Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
		cfg.setClassForTemplateLoading(getClass(), "/templates");
		cfg.setDefaultEncoding("UTF-8");

		EntityWriter writer = new EntityWriter(cfg);
		
		this.filename = "ExceptionHandler.java";
		
		this.content = writer.render(spec);

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