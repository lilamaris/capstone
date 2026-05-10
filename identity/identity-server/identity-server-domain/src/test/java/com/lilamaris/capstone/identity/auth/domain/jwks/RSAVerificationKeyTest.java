package com.lilamaris.capstone.identity.auth.domain.jwks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;

import static com.lilamaris.capstone.kernel.testsupport.assertion.DomainAssertions.assertThatDomainThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RSAVerificationKey 테스트")
public class RSAVerificationKeyTest {
    RSAPublicKey publicKey = generatePublicKey();

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {
        @Test
        @DisplayName("kid, publicKey로 생성한다")
        void create_with_kid_and_public_key() {
            var kid = "rsa-verification-key-1";

            var key = RSAVerificationKey.of(kid, publicKey);

            assertThat(key.kid()).isEqualTo(kid);
            assertThat(key.type()).isEqualTo(KeyType.VERIFIABLE);
            assertThat(key.signable()).isFalse();
            assertThat(key.getPublicKey()).isSameAs(publicKey);
        }

        @ParameterizedTest(name = "kid = {0}")
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("kid가 빈 문자열이면 예외")
        void throw_exception_when_blank_kid(String kid) {
            assertThatDomainThrownBy(() -> RSAVerificationKey.of(kid, publicKey))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasNonBlankMessageFor("kid");
        }

        @ParameterizedTest(name = "kid = {0}")
        @NullSource
        @DisplayName("kid가 null이면 예외")
        void throw_exception_when_null_kid(String kid) {
            assertThatDomainThrownBy(() -> RSAVerificationKey.of(kid, publicKey))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("kid");
        }

        @ParameterizedTest(name = "publicKey = {0}")
        @NullSource
        @DisplayName("publicKey가 null이면 예외")
        void throw_exception_when_null_public_key(RSAPublicKey publicKey) {
            assertThatDomainThrownBy(() -> RSAVerificationKey.of("rsa-verification-key-1", publicKey))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("publicKey");
        }
    }

    private static RSAPublicKey generatePublicKey() {
        try {
            var generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            return (RSAPublicKey) generator.generateKeyPair().getPublic();
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }
    }
}
