package com.example.specgen.writer;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.util.StringUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import com.example.specgen.model.Entity;
import com.example.specgen.model.Field;

public class ServiceWriter{
	private final Configuration cfg;

	public ServiceWriter(Configuration cfg){
		this.cfg = cfg;
	}

    public String render(Entity entity) throws Exception {
        Template template = cfg.getTemplate("service.ftl");

        Map<String, Object> model = new HashMap<>();
		model.put("package", entity.getPackage());
        model.put("entity", entity.getName());
		model.put("table", entity.getTable());
		model.put("fields", entity.getFields());
		model.put("primaryKey", entity.getPrimaryKey());
		model.put("primaryKeyType", entity.getFields().get(entity.getPrimaryKey()).getType());
		
        StringWriter out = new StringWriter();
        template.process(model, out);
        return out.toString();
    }
	
}