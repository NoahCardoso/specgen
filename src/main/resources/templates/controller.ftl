package ${package};
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
@RestController
@RequestMapping("/${table}")
public class ${entity}Controller {
    private final ${entity}Service service;
    public ${entity}Controller(${entity}Service service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<${entity}> create(
            @Valid @RequestBody ${entity} ${entity?uncap_first}<#rt>
<#list fields as fieldName, field>
<#if field.relation && (field.relationType == "ManyToOne" || field.relationType == "OneToOne")>,
            @RequestParam Long ${field.ref?uncap_first}Id<#rt>
</#if>
</#list>) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(${entity?uncap_first}<#rt>
<#list fields as fieldName, field>
<#if field.relation && (field.relationType == "ManyToOne" || field.relationType == "OneToOne")>, ${field.ref?uncap_first}Id<#rt>
</#if>
</#list>));
    }

    @GetMapping
    public List<${entity}> getAll() {
        return service.findAll();
    }

    @GetMapping("/{${primaryKey}}")
    public ResponseEntity<${entity}> getOne(@PathVariable ${primaryKeyType} ${primaryKey}) {
        return ResponseEntity.ok(service.findById(${primaryKey}));
    }

    @PutMapping("/{${primaryKey}}")
    public ResponseEntity<${entity}> update(
            @PathVariable ${primaryKeyType} ${primaryKey},
            @Valid @RequestBody ${entity} updated<#rt>
<#list fields as fieldName, field>
<#if field.relation && (field.relationType == "ManyToOne" || field.relationType == "OneToOne")>,
            @RequestParam Long ${field.ref?uncap_first}Id<#rt>
</#if>
</#list>) {
        return ResponseEntity.ok(service.update(${primaryKey}, updated<#rt>
<#list fields as fieldName, field>
<#if field.relation && (field.relationType == "ManyToOne" || field.relationType == "OneToOne")>, ${field.ref?uncap_first}Id<#rt>
</#if>
</#list>));
    }

    @DeleteMapping("/{${primaryKey}}")
    public ResponseEntity<Void> delete(@PathVariable ${primaryKeyType} ${primaryKey}) {
        service.delete(${primaryKey});
        return ResponseEntity.noContent().build();
    }
}