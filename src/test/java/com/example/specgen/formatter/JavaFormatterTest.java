package com.example.specgen.formatter;

import com.example.specgen.exception.UnsupportedTypeException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JavaFormatterTest {

    private JavaFormatter formatter;

    @Mock
    private Field field;

    @BeforeEach
    void setUp() {
        formatter = new JavaFormatter();
    }

    // =========================================================================
    // getJavaType
    // =========================================================================

    @Nested
    @DisplayName("getJavaType — type mappings")
    class GetJavaTypeMappings {

        @ParameterizedTest(name = "''{0}'' → ''String''")
        @DisplayName("string variants → String")
        @ValueSource(strings = { "string", "String", "STRING" })
        void stringVariants_returnString(String input) {
            when(field.getType()).thenReturn(input);
            assertThat(formatter.getJavaType(field)).isEqualTo("String");
        }

        @ParameterizedTest(name = "''{0}'' → ''int''")
        @DisplayName("int / integer variants → int")
        @ValueSource(strings = { "int", "INT", "Int", "integer", "Integer", "INTEGER" })
        void intVariants_returnInt(String input) {
            when(field.getType()).thenReturn(input);
            assertThat(formatter.getJavaType(field)).isEqualTo("int");
        }

        @ParameterizedTest(name = "''{0}'' → ''long''")
        @DisplayName("long variants → long")
        @ValueSource(strings = { "long", "Long", "LONG" })
        void longVariants_returnLong(String input) {
            when(field.getType()).thenReturn(input);
            assertThat(formatter.getJavaType(field)).isEqualTo("long");
        }

        @ParameterizedTest(name = "''{0}'' → ''boolean''")
        @DisplayName("boolean / bool variants → boolean")
        @ValueSource(strings = { "boolean", "Boolean", "BOOLEAN", "bool", "Bool", "BOOL" })
        void booleanVariants_returnBoolean(String input) {
            when(field.getType()).thenReturn(input);
            assertThat(formatter.getJavaType(field)).isEqualTo("boolean");
        }

        @ParameterizedTest(name = "''{0}'' → ''char''")
        @DisplayName("char / character variants → char")
        @ValueSource(strings = { "char", "Char", "CHAR", "character", "Character", "CHARACTER" })
        void charVariants_returnChar(String input) {
            when(field.getType()).thenReturn(input);
            assertThat(formatter.getJavaType(field)).isEqualTo("char");
        }

        @ParameterizedTest(name = "''{0}'' → ''double''")
        @DisplayName("double variants → double")
        @ValueSource(strings = { "double", "Double", "DOUBLE" })
        void doubleVariants_returnDouble(String input) {
            when(field.getType()).thenReturn(input);
            assertThat(formatter.getJavaType(field)).isEqualTo("double");
        }

        @ParameterizedTest(name = "''{0}'' → ''float''")
        @DisplayName("float variants → float")
        @ValueSource(strings = { "float", "Float", "FLOAT" })
        void floatVariants_returnFloat(String input) {
            when(field.getType()).thenReturn(input);
            assertThat(formatter.getJavaType(field)).isEqualTo("float");
        }

        @ParameterizedTest(name = "''{0}'' → ''UUID''")
        @DisplayName("uuid variants → UUID")
        @ValueSource(strings = { "uuid", "UUID", "Uuid" })
        void uuidVariants_returnUUID(String input) {
            when(field.getType()).thenReturn(input);
            assertThat(formatter.getJavaType(field)).isEqualTo("UUID");
        }
    }

    @Nested
    @DisplayName("getJavaType — whitespace handling")
    class GetJavaTypeWhitespace {

        @Test
        @DisplayName("Leading and trailing whitespace is stripped before matching")
        void leadingTrailingWhitespace_isStripped() {
            when(field.getType()).thenReturn("  string  ");
            assertThat(formatter.getJavaType(field)).isEqualTo("String");
        }

        @Test
        @DisplayName("Whitespace around an uppercase type is stripped and normalised")
        void whitespaceAroundUppercase_isStripped() {
            when(field.getType()).thenReturn("  LONG  ");
            assertThat(formatter.getJavaType(field)).isEqualTo("long");
        }
    }

    @Nested
    @DisplayName("getJavaType — unknown types throw")
    class GetJavaTypeUnknown {

        @Test
        @DisplayName("Unknown type throws UnsupportedTypeException")
        void unknownType_throwsUnsupportedTypeException() {
            when(field.getType()).thenReturn("blob");
            assertThatThrownBy(() -> formatter.getJavaType(field))
                    .isInstanceOf(UnsupportedTypeException.class)
                    .hasMessageContaining("blob");
        }

        @Test
        @DisplayName("Empty type string throws UnsupportedTypeException")
        void emptyType_throwsUnsupportedTypeException() {
            when(field.getType()).thenReturn("");
            assertThatThrownBy(() -> formatter.getJavaType(field))
                    .isInstanceOf(UnsupportedTypeException.class);
        }

        @Test
        @DisplayName("Whitespace-only type throws UnsupportedTypeException after stripping")
        void whitespaceOnlyType_throwsUnsupportedTypeException() {
            when(field.getType()).thenReturn("   ");
            assertThatThrownBy(() -> formatter.getJavaType(field))
                    .isInstanceOf(UnsupportedTypeException.class);
        }
    }

    // =========================================================================
    // toNonPrimitiveType
    // =========================================================================

    @Nested
    @DisplayName("toNonPrimitiveType — primitive → boxed type")
    class ToNonPrimitiveTypeMappings {

        @Test
        @DisplayName("'int' → 'Integer'")
        void int_returnsInteger() {
            assertThat(formatter.toNonPrimitiveType("int")).isEqualTo("Integer");
        }

        @Test
        @DisplayName("'char' → 'Character'")
        void char_returnsCharacter() {
            assertThat(formatter.toNonPrimitiveType("char")).isEqualTo("Character");
        }

        @Test
        @DisplayName("'boolean' → 'Boolean'")
        void boolean_returnsBoolean() {
            assertThat(formatter.toNonPrimitiveType("boolean")).isEqualTo("Boolean");
        }

        @Test
        @DisplayName("'long' → 'Long'")
        void long_returnsLong() {
            assertThat(formatter.toNonPrimitiveType("long")).isEqualTo("Long");
        }
    }

    @Nested
    @DisplayName("toNonPrimitiveType — whitespace handling")
    class ToNonPrimitiveTypeWhitespace {

        @Test
        @DisplayName("Leading/trailing whitespace stripped — ' int' → 'Integer'")
        void leadingSpace_isStrippedBeforeMatching() {
            assertThat(formatter.toNonPrimitiveType(" int")).isEqualTo("Integer");
        }

        @Test
        @DisplayName("Trailing whitespace stripped — 'long ' → 'Long'")
        void trailingSpace_isStrippedBeforeMatching() {
            assertThat(formatter.toNonPrimitiveType("long ")).isEqualTo("Long");
        }
    }

    @Nested
    @DisplayName("toNonPrimitiveType — blank / empty early-return")
    class ToNonPrimitiveTypeBlankHandling {

        @Test
        @DisplayName("Empty string returns empty string without throwing")
        void emptyString_returnsEmptyString() {
            assertThat(formatter.toNonPrimitiveType("")).isEqualTo("");
        }

        @Test
        @DisplayName("Blank string (spaces only) returns empty string after strip")
        void blankString_returnsEmptyAfterStrip() {
            // strip() now correctly applied; "   ".strip() → "" → isBlank() → return ""
            assertThat(formatter.toNonPrimitiveType("   ")).isEqualTo("");
        }
    }

    @Nested
    @DisplayName("toNonPrimitiveType — unknown types throw")
    class ToNonPrimitiveTypeUnknown {

        @Test
        @DisplayName("Non-primitive type 'String' throws UnsupportedTypeException")
        void String_throwsUnsupportedTypeException() {
            assertThatThrownBy(() -> formatter.toNonPrimitiveType("String"))
                    .isInstanceOf(UnsupportedTypeException.class)
                    .hasMessageContaining("String");
        }

        @Test
        @DisplayName("Unmapped primitive 'double' throws UnsupportedTypeException")
        void double_throwsUnsupportedTypeException() {
            assertThatThrownBy(() -> formatter.toNonPrimitiveType("double"))
                    .isInstanceOf(UnsupportedTypeException.class)
                    .hasMessageContaining("double");
        }

        @Test
        @DisplayName("Arbitrary unknown type throws UnsupportedTypeException")
        void unknownType_throwsUnsupportedTypeException() {
            assertThatThrownBy(() -> formatter.toNonPrimitiveType("blob"))
                    .isInstanceOf(UnsupportedTypeException.class)
                    .hasMessageContaining("blob");
        }
    }
}