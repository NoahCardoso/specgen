package com.example.specgen.parser;

import java.util.List;

import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.example.specgen.model.Entity;

@Component
public class YamlParser {

    public static List<Entity> parse(String yamlContent) {
        LoaderOptions options = new LoaderOptions();

        TypeDescription listType = new TypeDescription(EntityWrapper.class);
        listType.addPropertyParameters("entities", Entity.class);

        Constructor constructor = new Constructor(List.class, options);
        constructor.addTypeDescription(listType);

        Yaml yaml = new Yaml(constructor);

        return yaml.load(yamlContent);
    }
}
