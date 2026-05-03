package com.example.specgen.generator;
import com.example.specgen.formatter.JavaFormatter;
import com.example.specgen.model.Entity;
import com.example.specgen.model.Field;
import com.example.specgen.writer.ControllerWriter;
import freemarker.template.Configuration;
public class ControllerGenerator implements Generator{
	private final Entity spec;
	private String filename = "";
	private String content = "";

	public ControllerGenerator(Entity spec){
		this.spec = spec;
	}

	@Override
	public void generate() throws Exception{

		Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
		cfg.setClassForTemplateLoading(getClass(), "/templates");
		cfg.setDefaultEncoding("UTF-8");

		ControllerWriter writer = new ControllerWriter(cfg);
		
		this.filename = spec.getName() + "Controller.java";
		
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