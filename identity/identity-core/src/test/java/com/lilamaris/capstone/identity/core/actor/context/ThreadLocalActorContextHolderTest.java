package com.lilamaris.capstone.identity.core.actor.context;

import com.lilamaris.capstone.identity.core.actor.Actor;
import com.lilamaris.capstone.identity.core.actor.SimpleActor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ThreadLocalActorContextHolder 테스트")
public class ThreadLocalActorContextHolderTest {

    ActorContextHolder holder = new ThreadLocalActorContextHolder();

    @AfterEach
    void runClear() {
        holder.clear();
    }

    @Test
    @DisplayName("setActor 후 getActor로 같은 actor를 조회할 수 있다")
    void shouldRetrieveSameActorAfterSettingActor() {
        // given
        Actor actor = SimpleActor.of("user-1", Set.of());

        // when
        holder.setActor(actor);

        // then
        Actor actual = holder.getActor();
        assertThat(actual).isEqualTo(actor);
    }

    @Test
    @DisplayName("clear 후에 actor는 null이다")
    void shouldNotBeAbleToGetActorAfterClear() {
        // given
        holder.setActor(SimpleActor.of("user-1", Set.of()));

        // when
        holder.clear();

        // then
        assertThat(holder.getActor()).isNull();
    }

    @Test
    @DisplayName("Actor 정보는 ThreadLocal로 스레드 간 격리된다")
    void shouldIsolateActorInfoBetweenThreadsUsingThreadLocal() throws InterruptedException {
        // given
        holder.setActor(SimpleActor.of("main-user", Set.of()));

        var childThreadActor = new AtomicReference<Actor>();
        var latch = new CountDownLatch(1);

        Thread thread = new Thread(() -> {
            try {
                childThreadActor.set(holder.getActor());
            } finally {
                latch.countDown();
            }
        });

        // when
        thread.start();
        latch.await();

        // then
        assertThat(holder.getActor().subject()).isEqualTo("main-user");
        assertThat(childThreadActor.get()).isNull();
    }

    @Test
    @DisplayName("자식 스레드에서 설정한 actor는 해당 스레드에서만 보인다")
    void shouldOnlyBeVisibleToTargetThreadWhenActorIsSetInChildThread() throws InterruptedException {
        // given
        var mainSubject = "main-user";
        holder.setActor(SimpleActor.of(mainSubject, Set.of()));

        var childSubject = new AtomicReference<String>();
        var mainSubjectAfterChild = new AtomicReference<String>();
        var latch = new CountDownLatch(1);

        Thread thread = new Thread(() -> {
            try {
                holder.setActor(SimpleActor.of("child-user", Set.of()));
                childSubject.set(holder.getActor().subject());
            } finally {
                holder.clear();
                latch.countDown();
            }
        });

        // when
        thread.start();
        latch.await();
        mainSubjectAfterChild.set(holder.getActor().subject());

        // then
        assertThat(childSubject.get()).isEqualTo("child-user");
        assertThat(mainSubjectAfterChild.get()).isEqualTo("main-user");
    }
}
