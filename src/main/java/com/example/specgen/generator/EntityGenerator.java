package com.example.specgen.generator;
import com.example.specgen.formatter.JavaFormatter;
import com.example.specgen.model.Entity;
import com.example.specgen.model.Field;
import com.example.specgen.writer.EntityWriter;
public class EntityGenerator implements Generator{
	private final Entity spec;
	private String filename = "";
	private String content = "";
	public EntityGenerator(Entity spec){
		this.spec = spec;
	}

	@Override
	public void generate(){
		EntityWriter writer = new EntityWriter(new StringBuilder());
		JavaFormatter jf = new JavaFormatter();
		this.filename = spec.getName() + ".java";
		writer.addConstructor();
		for (String name: spec.getFields().keySet()){
			Field field = spec.getFields().get(name);
			String type = jf.getJavaType(field);
			writer.addGlobal(name, field);
			writer.addGetter(name, field);
			writer.addSetter(name, field);

		}
		writer.createClass(spec.getPackage(), spec.getName(), spec.getTable());
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