package com.example.specgen.writer;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.StringWriter;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class TemplateWriter {

    private final Configuration cfg;

    public TemplateWriter(Configuration cfg) {
        this.cfg = cfg;
    }

    public String render(String templateName, Map<String, Object> model) throws Exception {
        Template template = cfg.getTemplate(templateName);
        StringWriter out = new StringWriter();
        template.process(model, out);
        return out.toString();
    }
}