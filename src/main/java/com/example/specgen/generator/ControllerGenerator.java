package com.example.specgen.generator;
import com.example.specgen.formatter.JavaFormatter;
import com.example.specgen.model.Entity;
import com.example.specgen.model.Field;
import com.example.specgen.writer.ControllerWriter;

public class ControllerGenerator implements Generator{
	private final Entity spec;
	private String filename = "";
	private String content = "";

	public ControllerGenerator(Entity spec){
		this.spec = spec;
	}

	@Override
	public void generate(){
		ControllerWriter writer = new ControllerWriter(new StringBuilder());
		JavaFormatter jf = new JavaFormatter();
		this.filename = spec.getEntity() + "Controller.java";
		writer.addRepo(spec.getEntity());
		writer.addConstructor(spec.getEntity());

		String primaryKeyType = "";
		String primaryKey = "";
		for (String key: spec.getFields().keySet()){
			Field field = spec.getFields().get(key);
			if (field.isPrimary()){
				primaryKeyType = jf.getJavaType(field);
				primaryKey = key;
			}

		}

		if(primaryKeyType.equals("") || primaryKey.equals("")){
			//error
		}

		writer.addCreateRoute(spec.getEntity());
		writer.addGetAllRoute(spec.getEntity());
		writer.addGetOneRoute(spec.getEntity(), primaryKeyType, primaryKey);
		writer.addUpdateRoute(spec.getEntity(), primaryKeyType, primaryKey);
		writer.addDeleteRoute(spec.getEntity(), primaryKeyType, primaryKey);

		writer.createClass(spec.getEntity(), spec.getTable());
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