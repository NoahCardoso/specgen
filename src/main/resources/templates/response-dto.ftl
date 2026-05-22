package ${package};

public class ${entity}ResponseDto {

<#list fields as fieldName, field>
    <#if field.relation>
        <#if field.relationType == "ManyToOne" || field.relationType == "OneToOne">
    private Long ${field.ref?uncap_first}Id;
        </#if>
    <#else>
    private ${field.type} ${fieldName};
    </#if>
</#list>

    public static ${entity}ResponseDto fromEntity(${entity} ${entity?uncap_first}) {
        ${entity}ResponseDto dto = new ${entity}ResponseDto();
<#list fields as fieldName, field>
    <#if field.relation>
        <#if field.relationType == "ManyToOne" || field.relationType == "OneToOne">
        dto.set${field.ref}Id(
            ${entity?uncap_first}.get${fieldName?cap_first}() != null
                ? ${entity?uncap_first}.get${fieldName?cap_first}().getId()
                : null
        );
        </#if>
    <#else>
        dto.set${fieldName?cap_first}(${entity?uncap_first}.get${fieldName?cap_first}());
    </#if>
</#list>
        return dto;
    }

<#list fields as fieldName, field>
    <#if field.relation>
        <#if field.relationType == "ManyToOne" || field.relationType == "OneToOne">
    public Long get${field.ref}Id() {
        return ${field.ref?uncap_first}Id;
    }

    public void set${field.ref}Id(Long ${field.ref?uncap_first}Id) {
        this.${field.ref?uncap_first}Id = ${field.ref?uncap_first}Id;
    }

        </#if>
    <#else>
    public ${field.type} get${fieldName?cap_first}() {
        return ${fieldName};
    }

    public void set${fieldName?cap_first}(${field.type} ${fieldName}) {
        this.${fieldName} = ${fieldName};
    }

    </#if>
</#list>
}