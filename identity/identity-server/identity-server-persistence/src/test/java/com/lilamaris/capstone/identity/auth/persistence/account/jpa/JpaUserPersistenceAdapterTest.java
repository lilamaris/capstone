package com.lilamaris.capstone.identity.auth.persistence.account.jpa;

import com.lilamaris.capstone.identity.auth.domain.account.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaUserPersistenceAdapter.class)
@Tag("unit")
@DisplayName("JpaUserPersistenceAdapter JPA 테스트")
class JpaUserPersistenceAdapterTest {
    private static final Instant CREATED_AT = Instant.parse("2026-01-01T00:00:00Z");

    @Autowired
    private JpaUserPersistenceAdapter adapter;

    @Autowired
    private EntityManager entityManager;

    @Nested
    @DisplayName("사용자 저장")
    class SaveTest {
        @Test
        @DisplayName("사용자를 저장하고 식별자로 조회한다")
        void save_user_and_find_by_id() {
            User saved = adapter.save(User.of("tester", CREATED_AT));
            flushAndClear();

            assertThat(adapter.existsById(saved.getId())).isTrue();
            assertThat(adapter.findById(saved.getId()))
                    .hasValueSatisfying(found -> {
                        assertThat(found.getId()).isEqualTo(saved.getId());
                        assertThat(found.getNickname()).isEqualTo("tester");
                        assertThat(found.getCreatedAt()).isEqualTo(CREATED_AT);
                    });
        }
    }

    @Nested
    @DisplayName("사용자 삭제")
    class DeleteTest {
        @Test
        @DisplayName("사용자를 삭제한다")
        void delete_user() {
            User saved = adapter.save(User.of("tester", CREATED_AT));
            flushAndClear();

            adapter.delete(adapter.findById(saved.getId()).orElseThrow());
            flushAndClear();

            assertThat(adapter.existsById(saved.getId())).isFalse();
            assertThat(adapter.findById(saved.getId())).isEmpty();
        }
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
