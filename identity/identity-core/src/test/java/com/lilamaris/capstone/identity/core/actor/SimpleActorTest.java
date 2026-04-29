package com.lilamaris.capstone.identity.core.actor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static com.lilamaris.capstone.kernel.testsupport.assertion.DomainAssertions.assertThatDomainThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("SimpleActor 테스트")
public class SimpleActorTest {
    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {
        @Test
        @DisplayName("subject와 capabilities로 생성한다")
        void create_with_subject_and_capabilities() {
            var subject = "user-1";
            var capability = CapabilityFixture.createCapability();
            var actor = SimpleActor.of(subject, Set.of(capability));

            assertThat(actor.capabilities())
                    .containsExactly(capability);
        }

        @ParameterizedTest(name = "subject = {0}")
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("subject가 빈 문자열이면 예외")
        void throw_exception_when_blank_subject(String subject) {
            assertThatDomainThrownBy(() -> SimpleActor.of(subject, Set.of()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasNonBlankMessageFor("subject");
        }

        @ParameterizedTest(name = "subject = {0}")
        @NullSource
        @DisplayName("subject가 null이면 예외")
        void throw_exception_when_null_subject(String subject) {
            assertThatDomainThrownBy(() -> SimpleActor.of(subject, Set.of()))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("subject");
        }

        @ParameterizedTest(name = "capabilities = {0}")
        @NullSource
        @DisplayName("capabilities가 null이면 예외")
        void throw_exception_when_null_capabilities(Set<Capability> capabilities) {
            assertThatDomainThrownBy(() -> SimpleActor.of("subject", capabilities))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("capabilities");
        }

        @Test
        @DisplayName("capabilities에 null 원소 들어있으면 예외")
        void throw_exception_when_null_included_capabilities() {
            assertThatThrownBy(() -> SimpleActor.of("subject", Set.<Capability>of(null)))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("변환 테스트")
    class ConversionTest {
        @Test
        @DisplayName("다른 Actor 구현체에서 변환 가능")
        void convert_from_source() {
            var otherSubject = "other";
            var otherCapability = CapabilityFixture.createCapability();

            Actor other = new Actor() {
                @Override
                public String subject() {
                    return otherSubject;
                }

                @Override
                public Set<Capability> capabilities() {
                    return Set.of(otherCapability);
                }
            };

            var actor = SimpleActor.from(other);

            assertThat(actor.subject()).isEqualTo(otherSubject);
            assertThat(actor.capabilities())
                    .containsExactlyInAnyOrder(otherCapability);
        }

        @ParameterizedTest(name = "actor = {0}")
        @NullSource
        @DisplayName("변환 시 다른 Actor가 null이면 예외")
        void throw_exception_when_convert_source_is_null(Actor other) {
            assertThatDomainThrownBy(() -> SimpleActor.from(other))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("must not be null.");
        }
    }
}
