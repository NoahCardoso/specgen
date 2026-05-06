<#-- Expected:
     table: String
     fields: Map<String, Field>
     (key = column name, value = Field object)
-->

CREATE TABLE ${table} (
<#list fields?keys as name>
    <#assign field = fields[name]>
    ${name} ${field.type}
    <#if field.primary> PRIMARY KEY</#if>
    <#if field.unique> UNIQUE</#if>
    <#if !field.nullable> NOT NULL</#if>
    <#if name_has_next>,</#if>
</#list>
);