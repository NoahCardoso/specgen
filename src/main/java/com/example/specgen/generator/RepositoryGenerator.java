package com.example.specgen.generator;
import com.example.specgen.formatter.JavaFormatter;
import com.example.specgen.model.Entity;
import com.example.specgen.model.Field;
import com.example.specgen.writer.RepositoryWriter;

public class RepositoryGenerator implements Generator{
	private final Entity spec;
	private String filename;
	private String content;

	public RepositoryGenerator(Entity spec){
		this.spec = spec;
	}

	@Override
	public void generate(){
		RepositoryWriter writer = new RepositoryWriter(new StringBuilder());
		JavaFormatter jf = new JavaFormatter();
		this.filename = spec.getName() + "Repository.java";

		String primaryKeyType = "";
		
		for (String key: spec.getFields().keySet()){
			Field field = spec.getFields().get(key);
			if (field.isPrimary()){
				primaryKeyType = jf.toNonPrimitiveType(jf.getJavaType(field));
			}

		}
		
		writer.createRepository(spec.getPackage(), spec.getName(), primaryKeyType);

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