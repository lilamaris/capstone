package com.lilamaris.capstone.identity.auth.persistence.account;

import com.lilamaris.capstone.identity.auth.domain.account.CredentialAccount;
import com.lilamaris.capstone.identity.auth.domain.account.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        JpaCredentialAccountPersistenceAdapter.class,
        JpaUserPersistenceAdapter.class
})
@Tag("unit")
@DisplayName("JpaCredentialAccountPersistenceAdapter JPA 테스트")
class JpaCredentialAccountPersistenceAdapterTest {
    private static final Instant CREATED_AT = Instant.parse("2026-01-01T00:00:00Z");

    @Autowired
    private JpaCredentialAccountPersistenceAdapter adapter;

    @Autowired
    private JpaUserPersistenceAdapter userAdapter;

    @Autowired
    private EntityManager entityManager;

    @Nested
    @DisplayName("credential 계정 저장")
    class SaveTest {
        @Test
        @DisplayName("credential 계정을 저장하고 식별자, 이메일, 사용자 식별자로 조회한다")
        void save_credential_account_and_find_by_id_email_and_user_id() {
            User user = userAdapter.save(User.of("tester", CREATED_AT));
            CredentialAccount saved = adapter.save(CredentialAccount.of(
                    user,
                    "tester@example.com",
                    "{bcrypt}password-hash",
                    CREATED_AT
            ));
            flushAndClear();

            assertThat(adapter.existsByEmail("tester@example.com")).isTrue();
            assertThat(adapter.findById(saved.getId()))
                    .hasValueSatisfying(found -> assertCredentialAccount(found, saved.getId(), user.getId()));
            assertThat(adapter.findByEmail("tester@example.com"))
                    .hasValueSatisfying(found -> assertCredentialAccount(found, saved.getId(), user.getId()));
            assertThat(adapter.findByUserId(user.getId()))
                    .hasValueSatisfying(found -> assertCredentialAccount(found, saved.getId(), user.getId()));
        }
    }

    @Nested
    @DisplayName("credential 계정 삭제")
    class DeleteTest {
        @Test
        @DisplayName("credential 계정을 삭제한다")
        void delete_credential_account() {
            User user = userAdapter.save(User.of("tester", CREATED_AT));
            CredentialAccount saved = adapter.save(CredentialAccount.of(
                    user,
                    "tester@example.com",
                    "{bcrypt}password-hash",
                    CREATED_AT
            ));
            flushAndClear();

            adapter.delete(adapter.findById(saved.getId()).orElseThrow());
            flushAndClear();

            assertThat(adapter.existsByEmail("tester@example.com")).isFalse();
            assertThat(adapter.findById(saved.getId())).isEmpty();
            assertThat(adapter.findByEmail("tester@example.com")).isEmpty();
            assertThat(adapter.findByUserId(user.getId())).isEmpty();
        }
    }

    private static void assertCredentialAccount(CredentialAccount account, UUID accountId, UUID userId) {
        assertThat(account.getId()).isEqualTo(accountId);
        assertThat(account.getUser().getId()).isEqualTo(userId);
        assertThat(account.getEmail()).isEqualTo("tester@example.com");
        assertThat(account.getPasswordHash()).isEqualTo("{bcrypt}password-hash");
        assertThat(account.getCreatedAt()).isEqualTo(CREATED_AT);
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
