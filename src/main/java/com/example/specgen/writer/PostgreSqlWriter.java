package com.example.specgen.writer;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import freemarker.template.Template;

import org.springframework.util.StringUtils;

import com.example.specgen.formatter.PostgreSqlFormatter;
import com.example.specgen.model.Entity;
import com.example.specgen.model.Field;

import freemarker.template.Configuration;

public class PostgreSqlWriter{
	private final Configuration cfg;

    public PostgreSqlWriter(Configuration cfg) {
        this.cfg = cfg;
    }

    public String render(Entity entity) throws Exception {
        Template template = cfg.getTemplate("schema.ftl");

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
		
		StringWriter out = new StringWriter();
        template.process(model, out);
        return out.toString();
    }

}