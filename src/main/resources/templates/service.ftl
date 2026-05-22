package ${package};
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ${entity}Service {

    private final ${entity}Repository repo;
<#-- Inject a repository for each ManyToOne / OneToOne relation -->
<#list fields as fieldName, field>
<#if field.relation && (field.relationType == "ManyToOne" || field.relationType == "OneToOne")>
    private final ${field.ref}Repository ${field.ref?uncap_first}Repo;
</#if>
</#list>

    public ${entity}Service(
            ${entity}Repository repo<#rt>
<#list fields as fieldName, field>
<#if field.relation && (field.relationType == "ManyToOne" || field.relationType == "OneToOne")>,
            ${field.ref}Repository ${field.ref?uncap_first}Repo<#rt>
</#if>
</#list>) {
        this.repo = repo;
<#list fields as fieldName, field>
<#if field.relation && (field.relationType == "ManyToOne" || field.relationType == "OneToOne")>
        this.${field.ref?uncap_first}Repo = ${field.ref?uncap_first}Repo;
</#if>
</#list>
    }

    public ${entity} create(
            ${entity} ${entity?uncap_first}<#rt>
<#list fields as fieldName, field>
<#if field.relation && (field.relationType == "ManyToOne" || field.relationType == "OneToOne")>,
            Long ${field.ref?uncap_first}Id<#rt>
</#if>
</#list>) {
<#list fields as fieldName, field>
<#if field.relation && (field.relationType == "ManyToOne" || field.relationType == "OneToOne")>
        ${field.ref} ${field.ref?uncap_first} = ${field.ref?uncap_first}Repo.findById(${field.ref?uncap_first}Id)
            .orElseThrow(() -> new EntityNotFoundException("${field.ref} not found with id: " + ${field.ref?uncap_first}Id));
        ${entity?uncap_first}.set${fieldName?cap_first}(${field.ref?uncap_first});
</#if>
</#list>
        return repo.save(${entity?uncap_first});
    }

    public List<${entity}> findAll() {
        return repo.findAll();
    }

    public ${entity} findById(${primaryKeyType} ${primaryKey}) {
        return repo.findById(${primaryKey})
            .orElseThrow(() -> new EntityNotFoundException("${entity} not found with id: " + ${primaryKey}));
    }

    public ${entity} update(
            ${primaryKeyType} ${primaryKey},
            ${entity} updated<#rt>
<#list fields as fieldName, field>
<#if field.relation && (field.relationType == "ManyToOne" || field.relationType == "OneToOne")>,
            Long ${field.ref?uncap_first}Id<#rt>
</#if>
</#list>) {
        ${entity} existing = findById(${primaryKey});
<#list fields as fieldName, field>
    <#if !field.primary>
        <#if field.relation>
            <#if field.relationType == "ManyToOne" || field.relationType == "OneToOne">
        ${field.ref} ${field.ref?uncap_first} = ${field.ref?uncap_first}Repo.findById(${field.ref?uncap_first}Id)
            .orElseThrow(() -> new EntityNotFoundException("${field.ref} not found with id: " + ${field.ref?uncap_first}Id));
        existing.set${fieldName?cap_first}(${field.ref?uncap_first});
            </#if>
        <#else>
        existing.set${fieldName?cap_first}(updated.get${fieldName?cap_first}());
        </#if>
    </#if>
</#list>
        return repo.save(existing);
    }

    public void delete(${primaryKeyType} ${primaryKey}) {
        findById(${primaryKey});
        repo.deleteById(${primaryKey});
    }
}