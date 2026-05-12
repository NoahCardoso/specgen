package com.example.specgen.generator;

import com.example.specgen.formatter.JavaFormatter;
import com.example.specgen.model.Entity;
import com.example.specgen.model.Field;
import com.example.specgen.writer.TemplateWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntityGeneratorTest {

    @Mock private TemplateWriter writer;
    @Mock private JavaFormatter formatter;
    @Mock private Entity entity;

    @Captor private ArgumentCaptor<Map<String, Object>> modelCaptor;

    private EntityGenerator generator;

    // Reusable fields — using real objects since Field is a simple POJO
    private Field idField;
    private Field nameField;

    @BeforeEach
    void setUp() {
        generator = new EntityGenerator(writer, formatter);

        idField = new Field();
        idField.setType("long");
        idField.setPrimary(true);
        idField.setNullable(false);
        idField.setUnique(true);

        nameField = new Field();
        nameField.setType("string");
        nameField.setPrimary(false);
        nameField.setNullable(false);
        nameField.setUnique(false);
    }

    // =========================================================================
    // getName
    // =========================================================================

    @Nested
    @DisplayName("getName")
    class GetName {

        @Test
        @DisplayName("Returns entity name suffixed with '.java'")
        void returnsEntityNameDotJava() {
            when(entity.getName()).thenReturn("Product");
            generator.setEntity(entity);

            assertThat(generator.getName()).isEqualTo("Product.java");
        }

        @Test
        @DisplayName("Does not require generate() to have been called first")
        void doesNotRequireGenerate() {
            when(entity.getName()).thenReturn("Order");
            generator.setEntity(entity);

            // No generate() call — should still work
            assertThat(generator.getName()).isEqualTo("Order.java");
        }
    }

    // =========================================================================
    // getContent
    // =========================================================================

    @Nested
    @DisplayName("getContent")
    class GetContent {

        @Test
        @DisplayName("Returns null before generate() is called")
        void returnsNullBeforeGenerate() {
            generator.setEntity(entity);
            assertThat(generator.getContent()).isNull();
        }

        @Test
        @DisplayName("Returns the rendered template string after generate()")
        void returnsRenderedContentAfterGenerate() throws Exception {
            stubEntity("Product", "com.example", "products", "id", fields("id", idField));
            when(formatter.getJavaType(idField)).thenReturn("long");
            when(writer.render(eq("entity.ftl"), any())).thenReturn("rendered content");

            generator.generate();

            assertThat(generator.getContent()).isEqualTo("rendered content");
        }
    }

    // =========================================================================
    // generate — model construction
    // =========================================================================

    @Nested
    @DisplayName("generate — model keys")
    class GenerateModelKeys {

        @BeforeEach
        void setUpEntity() throws Exception {
            stubEntity("Product", "com.example", "products", "id", fields("id", idField));
            when(formatter.getJavaType(any(Field.class))).thenReturn("long");
            when(writer.render(eq("entity.ftl"), any())).thenReturn("");
        }

        @Test
        @DisplayName("Model contains correct package")
        void modelContainsPackage() throws Exception {
            generator.generate();
            assertThat(capturedModel().get("package")).isEqualTo("com.example");
        }

        @Test
        @DisplayName("Model contains entity name")
        void modelContainsEntityName() throws Exception {
            generator.generate();
            assertThat(capturedModel().get("entity")).isEqualTo("Product");
        }

        @Test
        @DisplayName("Model contains table name")
        void modelContainsTable() throws Exception {
            generator.generate();
            assertThat(capturedModel().get("table")).isEqualTo("products");
        }

        @Test
        @DisplayName("Model contains primaryKey")
        void modelContainsPrimaryKey() throws Exception {
            generator.generate();
            assertThat(capturedModel().get("primaryKey")).isEqualTo("id");
        }

        @Test
        @DisplayName("Model contains primaryKeyType as raw spec type from entity fields")
        void modelContainsPrimaryKeyType() throws Exception {
            // NOTE: primaryKeyType is pulled from entity.getFields().get(primaryKey).getType()
            // which is the raw spec type ("long"), NOT the formatted Java type.
            // If the template needs the Java type here, this is a bug in the generator.
            generator.generate();
            assertThat(capturedModel().get("primaryKeyType")).isEqualTo("long");
        }

        @Test
        @DisplayName("writer.render is called with 'entity.ftl'")
        void renderCalledWithEntityTemplate() throws Exception {
            generator.generate();
            verify(writer).render(eq("entity.ftl"), any());
        }
    }

    // =========================================================================
    // generate — field formatting
    // =========================================================================

    @Nested
    @DisplayName("generate — field formatting")
    class GenerateFieldFormatting {

        @Test
        @DisplayName("formatter.getJavaType is called for each field")
        void formatterCalledForEachField() throws Exception {
            LinkedHashMap<String, Field> entityFields = fields("id", idField, "name", nameField);
            stubEntity("Product", "com.example", "products", "id", entityFields);
            when(formatter.getJavaType(idField)).thenReturn("long");
            when(formatter.getJavaType(nameField)).thenReturn("String");
            when(writer.render(any(), any())).thenReturn("");

            generator.generate();

            verify(formatter).getJavaType(idField);
            verify(formatter).getJavaType(nameField);
        }

        @Test
        @DisplayName("Formatted field has the Java type returned by formatter")
        void formattedFieldHasJavaType() throws Exception {
            stubEntity("Product", "com.example", "products", "id", fields("id", idField));
            when(formatter.getJavaType(idField)).thenReturn("long");
            when(writer.render(any(), any())).thenReturn("");

            generator.generate();

            @SuppressWarnings("unchecked")
            Map<String, Field> formattedFields =
                    (Map<String, Field>) capturedModel().get("fields");

            assertThat(formattedFields.get("id").getType()).isEqualTo("long");
        }

        @Test
        @DisplayName("Formatted field preserves nullable from original field")
        void formattedFieldPreservesNullable() throws Exception {
            idField.setNullable(true);
            stubEntity("Product", "com.example", "products", "id", fields("id", idField));
            when(formatter.getJavaType(idField)).thenReturn("long");
            when(writer.render(any(), any())).thenReturn("");

            generator.generate();

            @SuppressWarnings("unchecked")
            Map<String, Field> formattedFields =
                    (Map<String, Field>) capturedModel().get("fields");

            assertThat(formattedFields.get("id").isNullable()).isTrue();
        }

        @Test
        @DisplayName("Formatted field preserves primary from original field")
        void formattedFieldPreservesPrimary() throws Exception {
            stubEntity("Product", "com.example", "products", "id", fields("id", idField));
            when(formatter.getJavaType(idField)).thenReturn("long");
            when(writer.render(any(), any())).thenReturn("");

            generator.generate();

            @SuppressWarnings("unchecked")
            Map<String, Field> formattedFields =
                    (Map<String, Field>) capturedModel().get("fields");

            assertThat(formattedFields.get("id").isPrimary()).isTrue();
        }

        @Test
        @DisplayName("Formatted field preserves unique from original field")
        void formattedFieldPreservesUnique() throws Exception {
            stubEntity("Product", "com.example", "products", "id", fields("id", idField));
            when(formatter.getJavaType(idField)).thenReturn("long");
            when(writer.render(any(), any())).thenReturn("");

            generator.generate();

            @SuppressWarnings("unchecked")
            Map<String, Field> formattedFields =
                    (Map<String, Field>) capturedModel().get("fields");

            assertThat(formattedFields.get("id").isUnique()).isTrue();
        }
    }

    // =========================================================================
    // generate — field order preserved
    // =========================================================================

    @Nested
    @DisplayName("generate — field order")
    class GenerateFieldOrder {

        @Test
        @DisplayName("Fields in model are in the same order as the entity's LinkedHashMap")
        void fieldOrderIsPreserved() throws Exception {
            LinkedHashMap<String, Field> entityFields =
                    fields("id", idField, "name", nameField);
            stubEntity("Product", "com.example", "products", "id", entityFields);
            when(formatter.getJavaType(idField)).thenReturn("long");
            when(formatter.getJavaType(nameField)).thenReturn("String");
            when(writer.render(any(), any())).thenReturn("");

            generator.generate();

            @SuppressWarnings("unchecked")
            Map<String, Field> formattedFields =
                    (Map<String, Field>) capturedModel().get("fields");

            assertThat(formattedFields.keySet())
                    .containsExactly("id", "name");
        }
    }

    // =========================================================================
    // generate — edge cases
    // =========================================================================

    @Nested
    @DisplayName("generate — edge cases")
    class GenerateEdgeCases {

        @Test
        @DisplayName("Calling generate() without setEntity() throws NullPointerException")
        void generateWithoutSetEntity_throwsNPE() {
            // entity is never set — generator.entity is null
            assertThatThrownBy(() -> generator.generate())
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("primaryKey not present in fields throws NullPointerException")
        void primaryKeyMissingFromFields_throwsNPE() {
            // primaryKey is "id" but fields only has "name" — .getType() on null → NPE
            stubEntity("Product", "com.example", "products", "id", fields("name", nameField));
            when(formatter.getJavaType(nameField)).thenReturn("String");

            assertThatThrownBy(() -> generator.generate())
                    .isInstanceOf(NullPointerException.class);
        }
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    private void stubEntity(
            String name,
            String pkg,
            String table,
            String primaryKey,
            LinkedHashMap<String, Field> fieldMap
    ) {
        when(entity.getName()).thenReturn(name);
        when(entity.getPackage()).thenReturn(pkg);
        when(entity.getTable()).thenReturn(table);
        when(entity.getPrimaryKey()).thenReturn(primaryKey);
        when(entity.getFields()).thenReturn(fieldMap);
        generator.setEntity(entity);
    }

    /** Capture the model map passed to writer.render. */
    @SuppressWarnings("unchecked")
    private Map<String, Object> capturedModel() throws Exception {
        verify(writer).render(any(), modelCaptor.capture());
        return modelCaptor.getValue();
    }

    /** Convenience builder for a LinkedHashMap of fields (preserves insertion order). */
    private static LinkedHashMap<String, Field> fields(String k1, Field f1) {
        LinkedHashMap<String, Field> map = new LinkedHashMap<>();
        map.put(k1, f1);
        return map;
    }

    private static LinkedHashMap<String, Field> fields(
            String k1, Field f1, String k2, Field f2
    ) {
        LinkedHashMap<String, Field> map = new LinkedHashMap<>();
        map.put(k1, f1);
        map.put(k2, f2);
        return map;
    }
}