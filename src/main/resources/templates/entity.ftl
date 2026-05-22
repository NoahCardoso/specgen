package ${package};

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "${table}")
public class ${entity} {

<#list fields as fieldName, field>
    <#if field.relation>
        @${field.relationType!"NULL_RELATION_TYPE"}
        @JoinColumn(name = "${field.joinColumn!"NULL_JOIN_COLUMN"}", nullable = ${field.nullable?c})
        private ${field.ref!"NULL_REF"} ${fieldName};
    <#else>
        <#-- Primary key -->
        <#if field.primary>
        @Id
        @GeneratedValue
        </#if>
        <#-- Column constraints -->
        <#if field.unique || !field.nullable>
            <#if !field.nullable>
                <#if field.type == "String">
        @NotBlank
                <#else>
        @NotNull
                </#if>
            </#if>
        @Column(
            <#if !field.nullable>nullable = false</#if>
            <#if field.unique><#if !field.nullable>, </#if>unique = true</#if>
        )
        </#if>
        private ${field.type} ${fieldName};
    </#if>
</#list>

<#-- Getters and Setters -->
<#list fields as fieldName, field>
    <#if field.relation>
    public ${field.ref!"NULL_REF"} get${fieldName?cap_first}() {
        return ${fieldName};
    }
    public void set${fieldName?cap_first}(${field.ref!"NULL_REF"} ${fieldName}) {
        this.${fieldName} = ${fieldName};
    }
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