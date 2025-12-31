package com.example.specgen.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.specgen.generator.ControllerGenerator;
import com.example.specgen.generator.EntityGenerator;
import com.example.specgen.generator.Generator;
import com.example.specgen.generator.PostgresSqlGenerator;
import com.example.specgen.generator.RepositoryGenerator;
import com.example.specgen.model.Entity;
import com.example.specgen.parser.YamlParser;
@Service
public class SpecService{

    public Map<String,String> process(String yaml) {
        Entity spec = YamlParser.parse(yaml);
        validate(spec);

        return generate(spec);

    }

    //TODO
    public void validate(Entity spec) throws InvalidSpecException{
        //need to enure they dont give me sql injects or smt
        if (spec.getEntity() == null) {
            throw new InvalidSpecException("Missing entity name");
        }
    }

    public Map<String,String> generate(Entity spec){
        Map<String,String> files = new HashMap<>();
        
        List<Generator> generators = List.of(
            new PostgresSqlGenerator(spec),
            new EntityGenerator(spec), 
            new ControllerGenerator(spec), 
            new RepositoryGenerator(spec)
        );

        for (Generator generator: generators){
            generator.generate();
            files.put(generator.getName(), generator.getContent());
        }

        return files;

    }

    private static class InvalidSpecException extends RuntimeException {

        public InvalidSpecException(String missing_entity_name) {
        }
    }

    

    



    
}