package com.lilamaris.capstone.identity.core.actor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.lilamaris.capstone.kernel.testsupport.assertion.DomainAssertions.assertThatDomainThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SimpleCapability 테스트")
public class SimpleCapabilityTest {
    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {
        @Test
        @DisplayName("scope와 description으로 생성")
        void create_with_scope_and_description() {
            var scope = "test.scope";
            var description = "test capability description";

            var capability = SimpleCapability.of(scope, description);

            assertThat(capability.scope()).isEqualTo(scope);
            assertThat(capability.description()).isEqualTo(description);
        }

        @ParameterizedTest(name = "scope = {0}")
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("scope가 빈 문자열이면 예외")
        void throw_exception_when_blank_scope(String scope) {
            assertThatDomainThrownBy(() -> SimpleCapability.of(scope, "description"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasNonBlankMessageFor("scope");
        }

        @ParameterizedTest(name = "scope = {0}")
        @NullSource
        @DisplayName("scope가 null이면 예외")
        void throw_exception_when_null_scope(String scope) {
            assertThatDomainThrownBy(() -> SimpleCapability.of(scope, "description"))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("scope");
        }

        @ParameterizedTest(name = "scope = {0}")
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("description이 빈 문자열이면 예외")
        void throw_exception_when_blank_description(String description) {
            assertThatDomainThrownBy(() -> SimpleCapability.of("scope", description))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasNonBlankMessageFor("description");
        }

        @ParameterizedTest(name = "scope = {0}")
        @NullSource
        @DisplayName("description이 null이면 예외")
        void throw_exception_when_null_description(String description) {
            assertThatDomainThrownBy(() -> SimpleCapability.of("scope", description))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("description");
        }
    }

    @Nested
    @DisplayName("변환 테스트")
    class ConversionTest {
        @Test
        @DisplayName("다른 Capability 구현체에서 변환 가능")
        void convert_from_source() {
            var otherScope = "scope";
            var otherDescription = "description";

            Capability other = new Capability() {
                @Override
                public String scope() {
                    return otherScope;
                }

                @Override
                public String description() {
                    return otherDescription;
                }
            };

            var capability = SimpleCapability.from(other);

            assertThat(capability.scope()).isEqualTo(otherScope);
            assertThat(capability.description()).isEqualTo(otherDescription);
        }

        @ParameterizedTest(name = "capability = {0}")
        @NullSource
        @DisplayName("변환 시 다른 Capability가 null이면 예외")
        void throw_exception_when_convert_source_is_null(Capability other) {
            assertThatDomainThrownBy(() -> SimpleCapability.from(other))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("must not be null.");
        }
    }
}
