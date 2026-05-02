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
		this.filename = spec.getName() + "Controller.java";
		writer.addRepo(spec.getName());
		writer.addConstructor(spec.getName());

		String primaryKeyType = "";
		String primaryKey = "";
		for (String key: spec.getFields().keySet()){
			Field field = spec.getFields().get(key);
			if (field.isPrimary()){
				primaryKeyType = jf.getJavaType(field);
				primaryKey = key;
			}

		}

		//C
		if (spec.isCreate()){
			writer.addCreateRoute(spec.getName());
		}
		//R
		if (spec.isRead()){
			writer.addGetAllRoute(spec.getName());
			writer.addGetOneRoute(spec.getName(), primaryKeyType, primaryKey);
		}
		//U
		if (spec.isUpdate()){
			writer.addUpdateRoute(spec.getName(), primaryKeyType, primaryKey, spec.getFields());
		}
		
		//D
		if(spec.isDelete()){
			writer.addDeleteRoute(spec.getName(), primaryKeyType, primaryKey);
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