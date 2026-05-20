package com.example.specgen.generator;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.example.specgen.formatter.JavaFormatter;
import com.example.specgen.model.Entity;
import com.example.specgen.model.Field;

import freemarker.template.Configuration;

import com.example.specgen.writer.EntityWriter;
import com.example.specgen.writer.TemplateWriter;
public class EntityGenerator implements Generator{
	private final TemplateWriter writer;
    private final JavaFormatter formatter;
    private Entity entity;
    private String content;

    public EntityGenerator(TemplateWriter writer, JavaFormatter formatter, Entity entity) {
        this.writer = writer;
        this.formatter = formatter;
		this.entity = entity;
    }

	public EntityGenerator(TemplateWriter writer, JavaFormatter formatter) {
        this.writer = writer;
        this.formatter = formatter;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

	@Override
	public void generate() throws Exception {
		

		Map<String, Object> model = new HashMap<>();
		model.put("package", entity.getPackage());
        model.put("entity", entity.getName());
		model.put("table", entity.getTable());
		Map<String,Field> formattedFields = new LinkedHashMap<>();
		
		for(String key: entity.getFields().keySet()){
			Field field = entity.getFields().get(key);
			Field formattedField = new Field();
			formattedField.setNullable(field.isNullable());
			formattedField.setPrimary(field.isPrimary());
			formattedField.setUnique(field.isUnique());
			formattedField.setType(formatter.getJavaType(field));
			formattedField.setRelationType(field.getRelationType());
			formattedField.setRef(field.getRef());
			formattedField.setJoinColumn(field.getJoinColumn());
			formattedFields.put(key,formattedField);
		}
        model.put("fields", formattedFields);
		model.put("primaryKey", entity.getPrimaryKey());
		model.put("primaryKeyType", entity.getFields().get(entity.getPrimaryKey()).getType());

		this.content = writer.render("entity.ftl",model);
	}

	@Override
	public String getContent(){
		return content;
	}

	@Override
	public String getName(){
		return entity.getName() + ".java";
	}
	
}