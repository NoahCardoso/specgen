package com.example.specgen.validator;

import com.example.specgen.exception.InvalidSpecException;
import com.example.specgen.formatter.JavaFormatter;
import com.example.specgen.formatter.PostgreSqlFormatter;
import com.example.specgen.model.Entity;

public class SpecValidator {

    private final JavaFormatter javaFormatter;
    private final PostgreSqlFormatter postgreSqlFormatter;
    private Entity spec;

    public SpecValidator(JavaFormatter javaFormatter, PostgreSqlFormatter postgreSqlFormatter) {
        this.javaFormatter = javaFormatter;
        this.postgreSqlFormatter = postgreSqlFormatter;
    }

    public void setSpec(Entity spec) {
        this.spec = spec;
    }

    public boolean check() throws InvalidSpecException {
        validateName();
        validateTable();
        validateFields();
        return true;
    }

    private void validateName() throws InvalidSpecException {
        if (spec.getName() == null) {
            throw new InvalidSpecException("Entity name must have a value");
        }
       	if (!spec.getName().matches("[A-Z][a-zA-Z0-9]*[a-z][a-zA-Z0-9]*")) {
            throw new InvalidSpecException("Invalid entity name: '" + spec.getName() + "'");
        }
    }

    private void validateTable() throws InvalidSpecException {
        if (spec.getTable() == null) {
            throw new InvalidSpecException("Table must have a value");
        }
        if (!spec.getTable().matches("[A-Za-z][A-Za-z0-9]*(_[A-Za-z0-9]+)*")) {
            throw new InvalidSpecException("Invalid table name: '" + spec.getTable() + "'");
        }
    }

    private void validateFields() throws InvalidSpecException {
        int primaryCount = 0;

        for (String key : spec.getFields().keySet()) {
            if (!key.matches("[A-Za-z]+[A-Za-z0-9]*")) {
                throw new InvalidSpecException("Invalid field name: '" + key + "'");
            }

            String specType = spec.getFields().get(key).getType();
            if (specType == null || !specType.matches("[A-Za-z]+[A-Za-z]*")) {
                throw new InvalidSpecException("Field type required for field: '" + key + "'");
            }

            javaFormatter.getJavaType(spec.getFields().get(key));     // throws UnsupportedTypeException if unsupported
            postgreSqlFormatter.getPostgreSqlType(spec.getFields().get(key));

            if (spec.getFields().get(key).isPrimary()) {
                primaryCount++;
            }
        }

        if (primaryCount != 1) {
            throw new InvalidSpecException("Entity must have exactly one primary key, found: " + primaryCount);
        }
    }
}