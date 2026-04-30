package com.lilamaris.capstone.identity.auth.application.jwks;

import com.lilamaris.capstone.identity.auth.application.jwks.contract.TokenIssuerMetadata;
import com.lilamaris.capstone.identity.auth.application.jwks.port.out.JwksReader;
import com.lilamaris.capstone.identity.auth.application.jwks.service.JoseIssueJwtUseCase;
import com.lilamaris.capstone.identity.auth.application.jwks.service.JwksService;
import com.lilamaris.capstone.identity.auth.application.jwks.service.RandomIssueOpaqueTokenUseCase;
import com.lilamaris.capstone.identity.auth.domain.jwks.RSAVerificationKey;
import com.lilamaris.capstone.kernel.testsupport.FixedClock;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.security.interfaces.RSAPublicKey;
import java.time.Clock;
import java.time.Duration;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.random.RandomGenerator;

import static com.lilamaris.capstone.kernel.testsupport.assertion.DomainAssertions.assertThatDomainThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
@DisplayName("Jwks 유스케이스 흐름 테스트")
class JwksUseCaseTest {
    static final Clock CLOCK = FixedClock.getFixed();
    static final String ISSUER = "https://auth.example.com";
    static final Duration EXPIRATION = Duration.ofHours(1);
    static final String SUBJECT = "user-1";
    static final Set<String> SCOPES = Set.of("profile", "timeline.read");
    static final String TOKEN_VALUE = "jwt-token";

    @Mock
    JwtEncoder jwtEncoder;

    @Mock
    RandomGenerator generator;

    @Mock
    JwksReader reader;

    @Mock
    RSAPublicKey publicKey;

    @Nested
    @DisplayName("JWT 발급")
    class IssueJwtTest {
        JoseIssueJwtUseCase service;

        @BeforeEach
        void setUp() {
            service = new JoseIssueJwtUseCase(
                    jwtEncoder,
                    new TokenIssuerMetadata(ISSUER, EXPIRATION),
                    CLOCK
            );
        }

        @Test
        @DisplayName("JWT claims를 구성하고 encoder로 발급한다")
        void build_claims_and_issue_by_encoder() {
            var parameters = ArgumentCaptor.forClass(JwtEncoderParameters.class);
            when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt());

            var token = service.issue(SUBJECT, SCOPES);

            assertThat(token).isEqualTo(TOKEN_VALUE);
            verify(jwtEncoder).encode(parameters.capture());

            var claims = parameters.getValue().getClaims();
            assertThat(claims.getIssuer().toString()).isEqualTo(ISSUER);
            assertThat(claims.getIssuedAt()).isEqualTo(CLOCK.instant());
            assertThat(claims.getExpiresAt()).isEqualTo(CLOCK.instant().plus(EXPIRATION));
            assertThat(claims.getSubject()).isEqualTo(SUBJECT);
            assertThat(claims.getClaims().get("scopes")).isEqualTo(SCOPES);
        }

        @ParameterizedTest(name = "subject = {0}")
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("subject가 빈 문자열이면 예외")
        void throw_exception_when_blank_subject(String subject) {
            assertThatDomainThrownBy(() -> service.issue(subject, SCOPES))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasNonBlankMessageFor("subject");
        }

        @ParameterizedTest(name = "subject = {0}")
        @NullSource
        @DisplayName("subject가 null이면 예외")
        void throw_exception_when_null_subject(String subject) {
            assertThatDomainThrownBy(() -> service.issue(subject, SCOPES))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("subject");
        }

        @ParameterizedTest(name = "scopes = {0}")
        @NullSource
        @DisplayName("scopes가 null이면 예외")
        void throw_exception_when_null_scopes(Set<String> scopes) {
            assertThatDomainThrownBy(() -> service.issue(SUBJECT, scopes))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("scopes");
        }
    }

    @Nested
    @DisplayName("opaque token 발급")
    class IssueOpaqueTokenTest {
        RandomIssueOpaqueTokenUseCase service;

        @BeforeEach
        void setUp() {
            service = new RandomIssueOpaqueTokenUseCase(generator);
        }

        @Test
        @DisplayName("랜덤 바이트를 URL safe base64 token으로 발급한다")
        void issue_url_safe_base64_token_from_random_bytes() {
            var randomBytes = new byte[32];
            Arrays.fill(randomBytes, (byte) 1);
            doAnswer(invocation -> {
                byte[] target = invocation.getArgument(0);
                System.arraycopy(randomBytes, 0, target, 0, randomBytes.length);
                return null;
            }).when(generator).nextBytes(any(byte[].class));

            var token = service.issue();

            assertThat(token).isEqualTo(Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes));
            verify(generator).nextBytes(any(byte[].class));
        }
    }

    @Nested
    @DisplayName("검증 키 목록 조회")
    class ListVerifiableKeyTest {
        JwksService service;

        @BeforeEach
        void setUp() {
            service = new JwksService(reader);
        }

        @Test
        @DisplayName("reader에서 검증 가능한 키 목록을 조회한다")
        void find_verifiable_keys_from_reader() {
            var keys = List.of(RSAVerificationKey.of("rsa-verification-key-1", publicKey));
            when(reader.findVerifiableKeys()).thenReturn(keys);

            var result = service.list();

            assertThat(result).isSameAs(keys);
            verify(reader).findVerifiableKeys();
        }
    }

    private static Jwt jwt() {
        return new Jwt(
                TOKEN_VALUE,
                CLOCK.instant(),
                CLOCK.instant().plus(EXPIRATION),
                Map.of("alg", "RS256"),
                Map.of("sub", SUBJECT)
        );
    }
}
