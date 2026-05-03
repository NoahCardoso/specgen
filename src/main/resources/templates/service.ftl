package ${mvnPackage};

import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@Service
public class ${entity}Service {

    private final ${entity}Repository repo;

    public ${entity}Service(${entity}Repository repo) {
        this.repo = repo;
    }

    public ${entity} create(${entity} ${entity?uncap_first}) {
        return repo.save(${entity?uncap_first});
    }

    public List<${entity}> findAll() {
        return repo.findAll();
    }

    public ${entity} findById(${primaryKeyType} ${primaryKey}) {
        return repo.findById(${primaryKey})
            .orElseThrow(() -> new EntityNotFoundException("${entity} not found with id: " + ${primaryKey}));
    }

    public ${entity} update(${primaryKeyType} ${primaryKey}, ${entity} updated) {
        ${entity} existing = findById(${primaryKey});

<#list fields as fieldName, field>
    <#if !field.primary>
        existing.set${fieldName?cap_first}(updated.get${fieldName?cap_first}());
    </#if>
</#list>

        return repo.save(existing);
    }

    public void delete(${primaryKeyType} ${primaryKey}) {
        findById(${primaryKey});
        repo.deleteById(${primaryKey});
    }
}