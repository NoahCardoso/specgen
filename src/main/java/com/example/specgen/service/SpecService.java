package com.example.specgen.service;

import org.springframework.stereotype.Service;

import com.example.specgen.generator.PostgresSqlGenerator;
import com.example.specgen.model.Entity;
import com.example.specgen.parser.YamlParser;
@Service
public class SpecService{

    public void process(String yaml) {
        Entity spec = YamlParser.parse(yaml);
        validate(spec);

        generate(spec);

    }

    //TODO
    public void validate(Entity spec) throws InvalidSpecException{
        //need to enure they dont give me sql injects or smt
        if (spec.getEntity() == null) {
            throw new InvalidSpecException("Missing entity name");
        }
    }

    public void generate(Entity spec){
        PostgresSqlGenerator generator = new PostgresSqlGenerator(spec);
        System.out.println(generator.generate());
    }

    private static class InvalidSpecException extends RuntimeException {

        public InvalidSpecException(String missing_entity_name) {
        }
    }

    



    
}