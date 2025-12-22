package com.example.specgen.parser;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.example.specgen.model.Entity;
public class YamlParser {

    public static Entity parse(String yamlContent) {
        LoaderOptions options = new LoaderOptions();
        Constructor constructor = new Constructor(Entity.class, options);
        Yaml yaml = new Yaml(constructor);

        return yaml.load(yamlContent);
    }
}
