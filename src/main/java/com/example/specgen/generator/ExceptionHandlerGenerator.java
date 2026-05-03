package com.example.specgen.generator;

import com.example.specgen.model.Entity;
import com.example.specgen.writer.ExceptionHandlerWriter;

public class ExceptionHandlerGenerator implements Generator{
	private final Entity spec;
	private String filename = "";
	private String content = "";

    public ExceptionHandlerGenerator(Entity spec){
        this.spec = spec;
    }

	@Override
	public void generate(){
		ExceptionHandlerWriter writer = new ExceptionHandlerWriter(new StringBuilder());
		
		this.filename = "ExceptionHandler.java";
		writer.create(spec.getPackage());
		
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