package com.example.specgen.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.specgen.generator.ControllerGenerator;
import com.example.specgen.generator.EntityGenerator;
import com.example.specgen.generator.Generator;
import com.example.specgen.generator.PostgreSqlGenerator;
import com.example.specgen.generator.RepositoryGenerator;
import com.example.specgen.model.Entity;
import com.example.specgen.parser.YamlParser;
import com.example.specgen.validator.SpecValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.specgen.generator.ExceptionHandlerGenerator;

@Service
public class SpecService{
    
    private static final Logger log = LoggerFactory.getLogger(SpecService.class);

    public Map<String,String> process(String yaml) throws Exception{
        Entity spec = YamlParser.parse(yaml);
        if (!validate(spec)){
            throw new Exception("Invalid yaml format");
        }

        log.info("yaml has been parsed into the service");

        return generate(spec);

    }

    //TODO
    public boolean validate(Entity spec) throws Exception{
        SpecValidator validator = new SpecValidator(spec);
        return validator.check();
        
    }

    public Map<String,String> generate(Entity spec){
        Map<String,String> files = new HashMap<>();
        
        log.info("Starting spec generation");

        List<Generator> generators;
        generators = List.of(
                new PostgreSqlGenerator(spec),
                new EntityGenerator(spec),
                new ControllerGenerator(spec),
                new RepositoryGenerator(spec),
                new ExceptionHandlerGenerator(spec)
        );

        for (Generator generator: generators){
            generator.generate();
            files.put(generator.getName(), generator.getContent());
        }

        return files;

    }
    
}