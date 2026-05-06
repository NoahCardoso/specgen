package com.example.specgen.generator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.example.specgen.formatter.PostgreSqlFormatter;
import com.example.specgen.model.Entity;
import com.example.specgen.model.Field;
import com.example.specgen.writer.PostgreSqlWriter;
import com.example.specgen.writer.TemplateWriter;

import freemarker.template.Template;
public class PostgreSqlGenerator implements Generator {

    private final TemplateWriter writer;
    private final PostgreSqlFormatter formatter;
    private Entity entity;
    private String content;

    public PostgreSqlGenerator(TemplateWriter writer, PostgreSqlFormatter formatter) {
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
        model.put("entity", entity.getName());
        model.put("table", entity.getTable());

        Map<String,Field> formatedFields = new LinkedHashMap<>();
		PostgreSqlFormatter pf = new PostgreSqlFormatter();
		for(String key: entity.getFields().keySet()){
			Field field = entity.getFields().get(key);
			Field formatedField = new Field();
			formatedField.setNullable(field.isNullable());
			formatedField.setPrimary(field.isPrimary());
			formatedField.setUnique(field.isUnique());
			formatedField.setType(pf.getPostgreSqlType(field));
			formatedFields.put(key,formatedField);
		}
        model.put("fields", formatedFields);

        this.content = writer.render("schema.ftl",model);
    }

    @Override public String getContent() { return content; }
    @Override public String getName() { return "schema.sql"; }
}