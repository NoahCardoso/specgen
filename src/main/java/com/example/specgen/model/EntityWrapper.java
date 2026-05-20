package com.example.specgen.model;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.example.specgen.model.Entity;

public class EntityWrapper {

    private List<Entity> entities;

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public List<Entity> getAll() {
        return entities;
    }

}