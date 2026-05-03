package com.example.specgen.writer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.example.specgen.generator.ExceptionHandlerGenerator;
import com.example.specgen.model.Entity;
import freemarker.template.Configuration;
import freemarker.template.Template;
public class ExceptionHandlerWriter {

    private final Configuration cfg;

    public ExceptionHandlerWriter(Configuration cfg) {
        this.cfg = cfg;
    }

    public String render(Entity entity) throws Exception {
        Template template = cfg.getTemplate("exception-handler.ftl");

        Map<String, Object> model = new HashMap<>();
		model.put("package", entity.getPackage());
        StringWriter out = new StringWriter();
        template.process(model, out);
        return out.toString();
    }

}