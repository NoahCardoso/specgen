package com.example.specgen.parser;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.example.specgen.model.Entity;
import com.example.specgen.model.EntityWrapper;

@Component
public class YamlParser {

    public static EntityWrapper parse(String yamlContent) {
        LoaderOptions options = new LoaderOptions();

        Constructor constructor =
                new Constructor(EntityWrapper.class, options);

        TypeDescription wrapperDesc =
                new TypeDescription(EntityWrapper.class);

        wrapperDesc.addPropertyParameters(
                "entities",
                Entity.class
        );

        constructor.addTypeDescription(wrapperDesc);

        Yaml yaml = new Yaml(constructor);

        return yaml.load(yamlContent);
    }
}
