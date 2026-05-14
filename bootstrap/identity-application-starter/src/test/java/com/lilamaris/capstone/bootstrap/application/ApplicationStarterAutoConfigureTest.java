package com.lilamaris.capstone.bootstrap.application;

import com.lilamaris.capstone.identity.core.actor.context.ActorContextHolder;
import com.lilamaris.capstone.identity.core.actor.context.ThreadLocalActorContextHolder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.time.Clock;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ApplicationStarterAutoConfigure 테스트")
class ApplicationStarterAutoConfigureTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(ApplicationStarterAutoConfigure.class));

    @Nested
    @DisplayName("자동 설정 테스트")
    class AutoConfigureTest {
        @Test
        @DisplayName("application 기본 bean을 등록한다")
        void register_default_application_beans() {
            contextRunner.run(context -> {
                assertThat(context).hasSingleBean(Clock.class);
                assertThat(context).hasSingleBean(ActorContextHolder.class);
                assertThat(context.getBean(ActorContextHolder.class))
                        .isInstanceOf(ThreadLocalActorContextHolder.class);
            });
        }

        @Test
        @DisplayName("timezone 설정으로 Clock zone을 구성한다")
        void configures_clock_zone_from_property() {
            contextRunner
                    .withPropertyValues("capstone.bootstrap.application.timezone=Asia/Seoul")
                    .run(context -> assertThat(context.getBean(Clock.class).getZone())
                            .isEqualTo(ZoneId.of("Asia/Seoul")));
        }

        @Test
        @DisplayName("이미 등록된 bean이 있으면 기본 bean으로 덮어쓰지 않는다")
        void does_not_override_existing_beans() {
            var customClock = Clock.system(ZoneId.of("Asia/Seoul"));
            var customActorContextHolder = new ThreadLocalActorContextHolder();

            contextRunner
                    .withBean(Clock.class, () -> customClock)
                    .withBean(ActorContextHolder.class, () -> customActorContextHolder)
                    .run(context -> {
                        assertThat(context.getBean(Clock.class)).isSameAs(customClock);
                        assertThat(context.getBean(ActorContextHolder.class))
                                .isSameAs(customActorContextHolder);
                    });
        }

        @Test
        @DisplayName("capstone.bootstrap.application.enabled가 false이면 자동 설정하지 않는다")
        void does_not_register_beans_when_application_starter_is_disabled() {
            contextRunner
                    .withPropertyValues("capstone.bootstrap.application.enabled=false")
                    .run(context -> {
                        assertThat(context).doesNotHaveBean(Clock.class);
                        assertThat(context).doesNotHaveBean(ActorContextHolder.class);
                    });
        }
    }
}
