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
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceGeneratorTest {

    @Mock private TemplateWriter writer;
    @Mock private JavaFormatter formatter;
    @Mock private Entity entity;

    @Captor private ArgumentCaptor<Map<String, Object>> modelCaptor;

    private ServiceGenerator generator;

    private Field idField;
    private Field nameField;

    @BeforeEach
    void setUp() {
        generator = new ServiceGenerator(writer, formatter);

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
        @DisplayName("Returns entity name suffixed with 'Service.java'")
        void returnsEntityNameServiceDotJava() {
            when(entity.getName()).thenReturn("Product");
            generator.setEntity(entity);

            assertThat(generator.getName()).isEqualTo("ProductService.java");
        }

        @Test
        @DisplayName("Does not require generate() to have been called first")
        void doesNotRequireGenerate() {
            when(entity.getName()).thenReturn("Order");
            generator.setEntity(entity);

            assertThat(generator.getName()).isEqualTo("OrderService.java");
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
            when(writer.render(eq("service.ftl"), any())).thenReturn("rendered service");

            generator.generate();

            assertThat(generator.getContent()).isEqualTo("rendered service");
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
            when(writer.render(eq("service.ftl"), any())).thenReturn("");
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
        @DisplayName("Model contains primaryKeyType as raw spec type")
        void modelContainsPrimaryKeyType() throws Exception {
            // NOTE: same issue as EntityGenerator — pulls from entity.getFields().get(primaryKey).getType()
            // which is the raw spec type ("long"), not the formatted Java type.
            generator.generate();
            assertThat(capturedModel().get("primaryKeyType")).isEqualTo("long");
        }

        @Test
        @DisplayName("writer.render is called with 'service.ftl'")
        void renderCalledWithServiceTemplate() throws Exception {
            generator.generate();
            verify(writer).render(eq("service.ftl"), any());
        }
    }

    // =========================================================================
    // generate — fields passed directly (no formatting)
    // =========================================================================

    @Nested
    @DisplayName("generate — fields are passed raw to the model")
    class GenerateRawFields {

        @Test
        @DisplayName("Model fields are the exact same map returned by entity.getFields()")
        void modelFieldsAreRawEntityFields() throws Exception {
            LinkedHashMap<String, Field> entityFields = fields("id", idField, "name", nameField);
            stubEntity("Product", "com.example", "products", "id", entityFields);
            when(writer.render(any(), any())).thenReturn("");

            generator.generate();

            @SuppressWarnings("unchecked")
            Map<String, Field> modelFields = (Map<String, Field>) capturedModel().get("fields");
            assertThat(modelFields).isSameAs(entityFields);
        }

        @Test
        @DisplayName("formatter is never used during generate()")
        void formatterIsNeverCalled() throws Exception {
            // NOTE: JavaFormatter is injected but unused — may indicate incomplete implementation
            // or a future formatting step. Pinned here so any accidental use is caught.
            stubEntity("Product", "com.example", "products", "id", fields("id", idField));
            when(writer.render(any(), any())).thenReturn("");

            generator.generate();

            verifyNoInteractions(formatter);
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
            assertThatThrownBy(() -> generator.generate())
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("primaryKey not present in fields map throws NullPointerException")
        void primaryKeyMissingFromFields_throwsNPE() {
            // primaryKey is "id" but only "name" exists — .getType() on null → NPE
            stubEntity("Product", "com.example", "products", "id", fields("name", nameField));

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

    @SuppressWarnings("unchecked")
    private Map<String, Object> capturedModel() throws Exception {
        verify(writer).render(any(), modelCaptor.capture());
        return modelCaptor.getValue();
    }

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