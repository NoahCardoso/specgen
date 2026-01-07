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
		this.filename = spec.getEntity() + ".java";
		writer.addConstructor();
		for (String key: spec.getFields().keySet()){
			Field field = spec.getFields().get(key);
			String type = jf.getJavaType(field);
			writer.addGlobal(type, key);
			writer.addGetter(type, key);
			writer.addSetter(type, key);

		}
		writer.createClass(spec.getMvnPackage(), spec.getEntity(), spec.getTable());
		this.content = writer.getStringFile();

	}

	@Override
	public String getContent(){
		if (content.isEmpty()){
			//error
		}
		return content;
	}

	@Override
	public String getName(){
		if (filename.isEmpty()){
			//error
		}
		return filename;
	}
	
}