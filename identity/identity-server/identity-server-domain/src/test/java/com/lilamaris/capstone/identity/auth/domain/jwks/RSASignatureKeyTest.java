package com.lilamaris.capstone.identity.auth.domain.jwks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;

import static com.lilamaris.capstone.kernel.testsupport.assertion.DomainAssertions.assertThatDomainThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RSASignatureKey 테스트")
public class RSASignatureKeyTest {
    RSAPrivateKey privateKey = generatePrivateKey();

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {
        @Test
        @DisplayName("kid, privateKey로 생성한다")
        void create_with_kid_and_private_key() {
            var kid = "rsa-signature-key-1";

            var key = RSASignatureKey.of(kid, privateKey);

            assertThat(key.kid()).isEqualTo(kid);
            assertThat(key.type()).isEqualTo(KeyType.SIGNABLE);
            assertThat(key.signable()).isTrue();
            assertThat(key.getPrivateKey()).isSameAs(privateKey);
        }

        @ParameterizedTest(name = "kid = {0}")
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("kid가 빈 문자열이면 예외")
        void throw_exception_when_blank_kid(String kid) {
            assertThatDomainThrownBy(() -> RSASignatureKey.of(kid, privateKey))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasNonBlankMessageFor("kid");
        }

        @ParameterizedTest(name = "kid = {0}")
        @NullSource
        @DisplayName("kid가 null이면 예외")
        void throw_exception_when_null_kid(String kid) {
            assertThatDomainThrownBy(() -> RSASignatureKey.of(kid, privateKey))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("kid");
        }

        @ParameterizedTest(name = "privateKey = {0}")
        @NullSource
        @DisplayName("privateKey가 null이면 예외")
        void throw_exception_when_null_private_key(RSAPrivateKey privateKey) {
            assertThatDomainThrownBy(() -> RSASignatureKey.of("rsa-signature-key-1", privateKey))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("privateKey");
        }
    }

    private static RSAPrivateKey generatePrivateKey() {
        try {
            var generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            return (RSAPrivateKey) generator.generateKeyPair().getPrivate();
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }
    }
}
