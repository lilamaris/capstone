package com.lilamaris.capstone.identity.core.role;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static com.lilamaris.capstone.kernel.testsupport.assertion.DomainAssertions.assertThatDomainThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SeparatorBasedNamespaceRoleDeserializer 테스트")
public class SeparatorBasedNamespaceRoleDeserializerTest {
    private final SeparatorBasedNamespaceRoleDeserializer deserializer = new SeparatorBasedNamespaceRoleDeserializer();

    @Nested
    @DisplayName("역직렬화 테스트")
    class DeserializationTest {
        @Test
        @DisplayName("namespace와 role을 구분자로 연결한 문자열을 namespaceRole로 역직렬화한다")
        void deserialize_namespace_role() {
            var deserialized = deserializer.deserialize("identity:ADMIN");

            assertThat(deserialized.namespace().name()).isEqualTo("identity");
            assertThat(deserialized.role()).isEqualTo(CanonicalRole.ADMIN);
        }

        @Test
        @DisplayName("문자열 컬렉션을 namespaceRole 집합으로 역직렬화한다")
        void deserialize_namespace_roles() {
            var deserialized = deserializer.deserialize(List.of("identity:ADMIN", "reservation:USER"));

            assertThat(deserialized)
                    .extracting(it -> it.namespace().name() + ":" + it.role().name())
                    .containsExactlyInAnyOrder("identity:ADMIN", "reservation:USER");
        }

        @ParameterizedTest(name = "source = {0}")
        @ValueSource(strings = {"", " ", "\t"})
        @DisplayName("source가 빈 문자열이면 예외")
        void throw_exception_when_blank_source(String source) {
            assertThatDomainThrownBy(() -> deserializer.deserialize(source))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasNonBlankMessageFor("source");
        }

        @ParameterizedTest(name = "source = {0}")
        @NullSource
        @DisplayName("source가 null이면 예외")
        void throw_exception_when_null_source(String source) {
            assertThatDomainThrownBy(() -> deserializer.deserialize(source))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("source");
        }

        @ParameterizedTest(name = "sources = {0}")
        @NullSource
        @DisplayName("source 컬렉션이 null이면 예외")
        void throw_exception_when_null_sources(List<String> sources) {
            assertThatDomainThrownBy(() -> deserializer.deserialize(sources))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("sources");
        }

        @ParameterizedTest(name = "source = {0}")
        @ValueSource(strings = {"identity", "identity:", ":ADMIN", "identity:ADMIN:extra"})
        @DisplayName("source 형식이 올바르지 않으면 예외")
        void throw_exception_when_invalid_source_format(String source) {
            assertThatDomainThrownBy(() -> deserializer.deserialize(source))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("invalid namespace role format");
        }

        @Test
        @DisplayName("role이 CanonicalRole이 아니면 예외")
        void throw_exception_when_invalid_role() {
            assertThatDomainThrownBy(() -> deserializer.deserialize("identity:MANAGER"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("invalid canonical role 'MANAGER'");
        }
    }
}
