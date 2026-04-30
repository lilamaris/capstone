package com.lilamaris.capstone.identity.auth.domain.jwks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.lilamaris.capstone.kernel.testsupport.assertion.DomainAssertions.assertThatDomainThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AbstractKeyMetadata 테스트")
public class AbstractKeyMetadataTest {
    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {
        @Test
        @DisplayName("kid, type으로 생성한다")
        void create_with_kid_and_type() {
            var kid = "rsa-key-1";
            var type = KeyType.SIGNABLE;

            var metadata = TestKeyMetadata.of(kid, type);

            assertThat(metadata.kid()).isEqualTo(kid);
            assertThat(metadata.type()).isEqualTo(type);
            assertThat(metadata.signable()).isTrue();
        }

        @Test
        @DisplayName("type이 VERIFIABLE이면 signable이 false")
        void return_false_when_type_is_verifiable() {
            var metadata = TestKeyMetadata.of("rsa-key-1", KeyType.VERIFIABLE);

            assertThat(metadata.signable()).isFalse();
        }

        @ParameterizedTest(name = "kid = {0}")
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("kid가 빈 문자열이면 예외")
        void throw_exception_when_blank_kid(String kid) {
            assertThatDomainThrownBy(() -> TestKeyMetadata.of(kid, KeyType.SIGNABLE))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasNonBlankMessageFor("kid");
        }

        @ParameterizedTest(name = "kid = {0}")
        @NullSource
        @DisplayName("kid가 null이면 예외")
        void throw_exception_when_null_kid(String kid) {
            assertThatDomainThrownBy(() -> TestKeyMetadata.of(kid, KeyType.SIGNABLE))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("kid");
        }

        @ParameterizedTest(name = "type = {0}")
        @NullSource
        @DisplayName("type이 null이면 예외")
        void throw_exception_when_null_type(KeyType type) {
            assertThatDomainThrownBy(() -> TestKeyMetadata.of("rsa-key-1", type))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("type");
        }
    }

    private static class TestKeyMetadata extends AbstractKeyMetadata {
        private TestKeyMetadata(String kid, KeyType type) {
            super(kid, type);
        }

        private static TestKeyMetadata of(String kid, KeyType type) {
            return new TestKeyMetadata(kid, type);
        }
    }
}
