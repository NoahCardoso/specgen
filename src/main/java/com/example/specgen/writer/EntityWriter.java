package com.example.specgen.writer;
import java.io.StringWriter;
import java.util.*;
import freemarker.template.Template;
import org.springframework.util.StringUtils;
import freemarker.template.Configuration;
import com.example.specgen.model.Entity;
import com.example.specgen.model.Field;

public class EntityWriter{
	private final Configuration cfg;

    public EntityWriter(Configuration cfg) {
        this.cfg = cfg;
    }

    public String render(Entity entity) throws Exception {
        Template template = cfg.getTemplate("entity.ftl");

        Map<String, Object> model = new HashMap<>();
		model.put("package", entity.getPackage());
        model.put("entity", entity.getName());
		model.put("table", entity.getTable());
		model.put("fields", entity.getFields());
        StringWriter out = new StringWriter();
        template.process(model, out);
        return out.toString();
    }

}