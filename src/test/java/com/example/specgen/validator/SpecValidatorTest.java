package com.example.specgen.validator;

import com.example.specgen.exception.InvalidSpecException;
import com.example.specgen.exception.UnsupportedTypeException;
import com.example.specgen.formatter.JavaFormatter;
import com.example.specgen.formatter.PostgreSqlFormatter;
import com.example.specgen.model.Entity;
import com.example.specgen.model.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpecValidatorTest {

    @Mock private JavaFormatter javaFormatter;
    @Mock private PostgreSqlFormatter postgreSqlFormatter;
    @Mock private Entity entity;

    private SpecValidator validator;

    @BeforeEach
    void setUp() {
        validator = new SpecValidator(javaFormatter, postgreSqlFormatter);
    }

    // =========================================================================
    // Happy path
    // =========================================================================

    @Nested
    @DisplayName("check — valid spec")
    class ValidSpec {

        @Test
        @DisplayName("Valid entity with one primary key returns true")
        void validSpec_returnsTrue() throws Exception {
            stubEntity("Product", "products", fields("id", primaryField("long")));
            stubFormattersReturnValid();

            assertThat(validator.check()).isTrue();
        }

        @Test
        @DisplayName("Valid entity with multiple fields returns true")
        void validSpec_multipleFields_returnsTrue() throws Exception {
            LinkedHashMap<String, Field> fieldMap = new LinkedHashMap<>();
            fieldMap.put("id",    primaryField("long"));
            fieldMap.put("name",  plainField("string"));
            fieldMap.put("price", plainField("double"));
            stubEntity("Product", "products", fieldMap);
            stubFormattersReturnValid();

            assertThat(validator.check()).isTrue();
        }
    }

    // =========================================================================
    // Name validation
    // =========================================================================

    @Nested
    @DisplayName("validateName")
    class ValidateName {

        @Test
        @DisplayName("Null name throws InvalidSpecException with 'must have a value' message")
        void nullName_throwsInvalidSpecException() {
            when(entity.getName()).thenReturn(null);
            validator.setSpec(entity);

            assertThatThrownBy(() -> validator.check())
                    .isInstanceOf(InvalidSpecException.class)
                    .hasMessageContaining("must have a value");
        }

        @ParameterizedTest(name = "''{0}'' fails name regex")
        @DisplayName("Invalid name formats throw InvalidSpecException")
        @ValueSource(strings = {
                "product",       // starts with lowercase
                "PRODUCT",       // all uppercase — regex requires lowercase after first char
                "123Product",    // starts with digit
                "Product Name",  // contains space
                "Product_Name",  // contains underscore
        })
        void invalidNameFormat_throwsInvalidSpecException(String name) {
            when(entity.getName()).thenReturn(name);
            validator.setSpec(entity);

            assertThatThrownBy(() -> validator.check())
                    .isInstanceOf(InvalidSpecException.class)
                    .hasMessageContaining("Invalid entity name");
        }

        @ParameterizedTest(name = "''{0}'' passes name regex")
        @DisplayName("Valid name formats pass name validation")
        @ValueSource(strings = { "Product", "Order", "OrderItem", "Product2" })
        void validNameFormat_doesNotThrowOnName(String name) {
            // Table is stubbed to null so it fails next — confirming name itself passed
            when(entity.getName()).thenReturn(name);
            when(entity.getTable()).thenReturn(null);
            validator.setSpec(entity);

            assertThatThrownBy(() -> validator.check())
                    .isInstanceOf(InvalidSpecException.class)
                    .hasMessageNotContaining("Invalid entity name")
                    .hasMessageNotContaining("name must have a value");
        }
    }

    // =========================================================================
    // Table validation
    // =========================================================================

    @Nested
    @DisplayName("validateTable")
    class ValidateTable {

        @Test
        @DisplayName("Null table throws InvalidSpecException with 'must have a value' message")
        void nullTable_throwsInvalidSpecException() {
            when(entity.getName()).thenReturn("Product");
            when(entity.getTable()).thenReturn(null);
            validator.setSpec(entity);

            assertThatThrownBy(() -> validator.check())
                    .isInstanceOf(InvalidSpecException.class)
                    .hasMessageContaining("must have a value");
        }

        @ParameterizedTest(name = "''{0}'' fails table regex")
        @DisplayName("Invalid table formats throw InvalidSpecException")
        @ValueSource(strings = {
                "123products",    // starts with digit
                "order items",    // contains space
                "order__items",   // double underscore
        })
        void invalidTableFormat_throwsInvalidSpecException(String table) {
            when(entity.getName()).thenReturn("Product");
            when(entity.getTable()).thenReturn(table);
            validator.setSpec(entity);

            assertThatThrownBy(() -> validator.check())
                    .isInstanceOf(InvalidSpecException.class)
                    .hasMessageContaining("Invalid table name");
        }

        @ParameterizedTest(name = "''{0}'' passes table regex")
        @DisplayName("Valid table formats pass table validation")
        @ValueSource(strings = { "products", "order_items", "orderItems", "Products2" })
        void validTableFormat_doesNotThrowOnTable(String table) {
            // Fields stubbed to empty so it fails on primary key count — confirming table passed
            when(entity.getName()).thenReturn("Product");
            when(entity.getTable()).thenReturn(table);
            when(entity.getFields()).thenReturn(new LinkedHashMap<>());
            validator.setSpec(entity);

            assertThatThrownBy(() -> validator.check())
                    .isInstanceOf(InvalidSpecException.class)
                    .hasMessageNotContaining("Invalid table name")
                    .hasMessageNotContaining("table name must have a value");
        }
    }

    // =========================================================================
    // Field name validation
    // =========================================================================

    @Nested
    @DisplayName("validateFields — field name")
    class ValidateFieldName {

        @ParameterizedTest(name = "''{0}'' is an invalid field name")
        @DisplayName("Field names starting with a digit or containing symbols throw InvalidSpecException")
        @ValueSource(strings = {
                "123id",      // starts with digit
                "my field",   // contains space
                "my_field",   // contains underscore — not permitted by regex
        })
        void invalidFieldName_throwsInvalidSpecException(String fieldName) {
            LinkedHashMap<String, Field> fieldMap = new LinkedHashMap<>();
            fieldMap.put(fieldName, primaryField("long"));
            stubEntity("Product", "products", fieldMap);

            assertThatThrownBy(() -> validator.check())
                    .isInstanceOf(InvalidSpecException.class)
                    .hasMessageContaining("Invalid field name");
        }
    }

    // =========================================================================
    // Field type validation
    // =========================================================================

    @Nested
    @DisplayName("validateFields — field type")
    class ValidateFieldType {

        @Test
        @DisplayName("Null field type throws InvalidSpecException")
        void nullFieldType_throwsInvalidSpecException() {
            Field field = new Field();
            field.setType(null);
            field.setPrimary(true);
            stubEntity("Product", "products", fields("id", field));

            assertThatThrownBy(() -> validator.check())
                    .isInstanceOf(InvalidSpecException.class)
                    .hasMessageContaining("Field type required");
        }

        @ParameterizedTest(name = "''{0}'' fails type regex")
        @DisplayName("Types containing non-alpha characters throw InvalidSpecException")
        @ValueSource(strings = {
                "int32",        // contains digit
                "big decimal",  // contains space
                "long_type",    // contains underscore
        })
        void invalidTypeFormat_throwsInvalidSpecException(String type) {
            stubEntity("Product", "products", fields("id", primaryField(type)));

            assertThatThrownBy(() -> validator.check())
                    .isInstanceOf(InvalidSpecException.class)
                    .hasMessageContaining("Field type required");
        }

        @Test
        @DisplayName("Type unsupported by JavaFormatter propagates UnsupportedTypeException")
        void typeUnsupportedByJavaFormatter_propagatesException() {
            stubEntity("Product", "products", fields("id", primaryField("blob")));
            when(javaFormatter.getJavaType(any(Field.class)))
                    .thenThrow(new UnsupportedTypeException("Unsupported type: 'blob'"));

            assertThatThrownBy(() -> validator.check())
                    .isInstanceOf(UnsupportedTypeException.class)
                    .hasMessageContaining("blob");
        }

        @Test
        @DisplayName("Type unsupported by PostgreSqlFormatter propagates UnsupportedTypeException")
        void typeUnsupportedByPostgreSqlFormatter_propagatesException() {
            stubEntity("Product", "products", fields("id", primaryField("long")));
            when(javaFormatter.getJavaType(any(Field.class))).thenReturn("long");
            when(postgreSqlFormatter.getPostgreSqlType(any(Field.class)))
                    .thenThrow(new UnsupportedTypeException("Unsupported type in PgSql"));

            assertThatThrownBy(() -> validator.check())
                    .isInstanceOf(UnsupportedTypeException.class);
        }
    }

    // =========================================================================
    // Primary key validation
    // =========================================================================

    @Nested
    @DisplayName("validateFields — primary key count")
    class ValidatePrimaryKey {

        @Test
        @DisplayName("No primary key throws InvalidSpecException with count 0")
        void noPrimaryKey_throwsWithCountZero() throws Exception {
            stubEntity("Product", "products", fields("id", plainField("long")));
            stubFormattersReturnValid();

            assertThatThrownBy(() -> validator.check())
                    .isInstanceOf(InvalidSpecException.class)
                    .hasMessageContaining("exactly one primary key")
                    .hasMessageContaining("0");
        }

        @Test
        @DisplayName("Two primary keys throws InvalidSpecException with count 2")
        void twoPrimaryKeys_throwsWithCountTwo() throws Exception {
            LinkedHashMap<String, Field> fieldMap = new LinkedHashMap<>();
            fieldMap.put("id",   primaryField("long"));
            fieldMap.put("uuid", primaryField("string"));
            stubEntity("Product", "products", fieldMap);
            stubFormattersReturnValid();

            assertThatThrownBy(() -> validator.check())
                    .isInstanceOf(InvalidSpecException.class)
                    .hasMessageContaining("exactly one primary key")
                    .hasMessageContaining("2");
        }

        @Test
        @DisplayName("Exactly one primary key among multiple fields passes")
        void onePrimaryKeyAmongManyFields_passes() throws Exception {
            LinkedHashMap<String, Field> fieldMap = new LinkedHashMap<>();
            fieldMap.put("id",    primaryField("long"));
            fieldMap.put("name",  plainField("string"));
            fieldMap.put("price", plainField("double"));
            stubEntity("Product", "products", fieldMap);
            stubFormattersReturnValid();

            assertThat(validator.check()).isTrue();
        }
    }

    // =========================================================================
    // setSpec guard
    // =========================================================================

    @Nested
    @DisplayName("setSpec — guard")
    class SetSpecGuard {

        @Test
        @DisplayName("Calling check() without setSpec() throws NullPointerException")
        void checkWithoutSetSpec_throwsNPE() {
            assertThatThrownBy(() -> validator.check())
                    .isInstanceOf(NullPointerException.class);
        }
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    private void stubEntity(String name, String table, LinkedHashMap<String, Field> fieldMap) {
        when(entity.getName()).thenReturn(name);
        when(entity.getTable()).thenReturn(table);
        when(entity.getFields()).thenReturn(fieldMap);
        validator.setSpec(entity);
    }

    private void stubFormattersReturnValid() {
        when(javaFormatter.getJavaType(any(Field.class))).thenReturn("long");
        when(postgreSqlFormatter.getPostgreSqlType(any(Field.class))).thenReturn("BIGINT");
    }

    private static Field primaryField(String type) {
        Field field = new Field();
        field.setType(type);
        field.setPrimary(true);
        field.setNullable(false);
        field.setUnique(false);
        return field;
    }

    private static Field plainField(String type) {
        Field field = new Field();
        field.setType(type);
        field.setPrimary(false);
        field.setNullable(true);
        field.setUnique(false);
        return field;
    }

    private static LinkedHashMap<String, Field> fields(String key, Field field) {
        LinkedHashMap<String, Field> map = new LinkedHashMap<>();
        map.put(key, field);
        return map;
    }
}