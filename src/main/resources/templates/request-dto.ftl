package ${package};

public class ${entity}RequestDto {

<#list fields as fieldName, field>
    <#if !field.primary>
        <#if field.relation>
            <#if field.relationType == "ManyToOne" || field.relationType == "OneToOne">
    private Long ${field.ref?uncap_first}Id;
            </#if>
        <#else>
    private ${field.type} ${fieldName};
        </#if>
    </#if>
</#list>

    public ${entity} toEntity() {
        ${entity} ${entity?uncap_first} = new ${entity}();
<#list fields as fieldName, field>
    <#if !field.primary && !field.relation>
        ${entity?uncap_first}.set${fieldName?cap_first}(this.${fieldName});
    </#if>
</#list>
        return ${entity?uncap_first};
    }

<#list fields as fieldName, field>
    <#if !field.primary>
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
    </#if>
</#list>
}