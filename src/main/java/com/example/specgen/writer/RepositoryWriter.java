package com.example.specgen.writer;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import freemarker.template.Template;
import org.springframework.util.StringUtils;

import com.example.specgen.model.Entity;
import com.example.specgen.model.Field;
import freemarker.template.Configuration;

public class RepositoryWriter{
	private final Configuration cfg;

    public RepositoryWriter(Configuration cfg) {
        this.cfg = cfg;
    }

    public String render(Entity entity) throws Exception {
        Template template = cfg.getTemplate("repository.ftl");

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