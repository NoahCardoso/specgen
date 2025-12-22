package com.example.specgen.service;

import org.springframework.stereotype.Service;

import com.example.specgen.model.Entity;
import com.example.specgen.parser.YamlParser;

@Service
public class SpecService{

    public void process(String yaml) {
        Entity spec = YamlParser.parse(yaml);
        validate(spec);
        
        //generate(spec);

    }

    public void validate(Entity spec) throws InvalidSpecException{
        if (spec.getEntity() == null) {
            throw new InvalidSpecException("Missing entity name");
        }
    }

    private static class InvalidSpecException extends RuntimeException {

        public InvalidSpecException(String missing_entity_name) {
        }
    }

    



    
}