package com.lilamaris.capstone.identity.auth.domain.role;

import com.lilamaris.capstone.kernel.core.namespace.ApplicationNamespace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.lilamaris.capstone.kernel.testsupport.assertion.DomainAssertions.assertThatDomainThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EmbeddableApplicationNamespace 테스트")
public class EmbeddableApplicationNamespaceTest {
    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {
        @Test
        @DisplayName("name으로 생성한다")
        void create_with_name() {
            var name = "reservation";

            var namespace = EmbeddableApplicationNamespace.of(name);

            assertThat(namespace.name()).isEqualTo(name);
            assertThat(namespace.getName()).isEqualTo(name);
        }

        @ParameterizedTest(name = "name = {0}")
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("name이 빈 문자열이면 예외")
        void throw_exception_when_blank_name(String name) {
            assertThatDomainThrownBy(() -> EmbeddableApplicationNamespace.of(name))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasNonBlankMessageFor("name");
        }

        @ParameterizedTest(name = "name = {0}")
        @NullSource
        @DisplayName("name이 null이면 예외")
        void throw_exception_when_null_name(String name) {
            assertThatDomainThrownBy(() -> EmbeddableApplicationNamespace.of(name))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("name");
        }
    }

    @Nested
    @DisplayName("변환 테스트")
    class ConversionTest {
        @Test
        @DisplayName("다른 ApplicationNamespace 구현체에서 변환 가능")
        void convert_from_source() {
            var otherName = "identity";

            ApplicationNamespace other = new ApplicationNamespace() {
                @Override
                public String name() {
                    return otherName;
                }
            };

            var namespace = EmbeddableApplicationNamespace.from(other);

            assertThat(namespace.name()).isEqualTo(otherName);
            assertThat(namespace.getName()).isEqualTo(otherName);
        }

        @ParameterizedTest(name = "applicationNamespace = {0}")
        @NullSource
        @DisplayName("변환 시 다른 ApplicationNamespace가 null이면 예외")
        void throw_exception_when_convert_source_is_null(ApplicationNamespace namespace) {
            assertThatDomainThrownBy(() -> EmbeddableApplicationNamespace.from(namespace))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("namespace");
        }

        @Test
        @DisplayName("변환 시 다른 ApplicationNamespace의 name이 빈 문자열이면 예외")
        void throw_exception_when_convert_source_name_is_blank() {
            ApplicationNamespace other = new ApplicationNamespace() {
                @Override
                public String name() {
                    return " ";
                }
            };

            assertThatDomainThrownBy(() -> EmbeddableApplicationNamespace.from(other))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasNonBlankMessageFor("name");
        }

        @Test
        @DisplayName("변환 시 다른 ApplicationNamespace의 name이 null이면 예외")
        void throw_exception_when_convert_source_name_is_null() {
            ApplicationNamespace other = new ApplicationNamespace() {
                @Override
                public String name() {
                    return null;
                }
            };

            assertThatDomainThrownBy(() -> EmbeddableApplicationNamespace.from(other))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("name");
        }
    }

    @Nested
    @DisplayName("동일성 비교 테스트")
    class SameTest {
        @Test
        @DisplayName("name이 같으면 같은 namespace로 판단한다")
        void return_true_when_name_is_same() {
            var namespace = EmbeddableApplicationNamespace.of("reservation");
            var other = EmbeddableApplicationNamespace.of("reservation");

            var result = namespace.isSame(other);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("name이 다르면 다른 namespace로 판단한다")
        void return_false_when_name_is_different() {
            var namespace = EmbeddableApplicationNamespace.of("reservation");
            var other = EmbeddableApplicationNamespace.of("identity");

            var result = namespace.isSame(other);

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("다른 ApplicationNamespace 구현체와 name이 같으면 같은 namespace로 판단한다")
        void return_true_when_other_implementation_has_same_name() {
            var namespace = EmbeddableApplicationNamespace.of("reservation");

            ApplicationNamespace other = new ApplicationNamespace() {
                @Override
                public String name() {
                    return "reservation";
                }
            };

            var result = namespace.isSame(other);

            assertThat(result).isTrue();
        }

        @ParameterizedTest(name = "namespace = {0}")
        @NullSource
        @DisplayName("비교 대상 namespace가 null이면 예외")
        void throw_exception_when_compare_target_is_null(ApplicationNamespace namespace) {
            var source = EmbeddableApplicationNamespace.of("reservation");

            assertThatDomainThrownBy(() -> source.isSame(namespace))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("namespace");
        }
    }
}
