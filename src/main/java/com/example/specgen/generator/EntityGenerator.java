package com.example.specgen.generator;
import com.example.specgen.formatter.JavaFormatter;
import com.example.specgen.model.Entity;
import com.example.specgen.model.Field;
import com.example.specgen.writer.EntityWriter;
public class EntityGenerator implements Generator{
	private final Entity spec;
	private String fileName;

	public EntityGenerator(Entity spec){
		this.spec = spec;
	}

	@Override
	public void generate(){
		EntityWriter writer = new EntityWriter(new StringBuilder());
		JavaFormatter jf = new JavaFormatter();
		fileName = spec.getEntity() + ".java";
		for (String key: spec.getFields().keySet()){
			Field field = spec.getFields().get(key);
			String type = jf.getJavaType(field);
			writer.addGlobal(type, key);
			writer.addGetter(type, key);
			writer.addSetter(type, key);

		}
		writer.createClass(spec.getEntity(), spec.getTable());
		writer.toFile(fileName);

	}
}