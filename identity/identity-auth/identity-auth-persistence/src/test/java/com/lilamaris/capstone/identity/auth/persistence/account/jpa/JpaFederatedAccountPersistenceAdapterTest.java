package com.lilamaris.capstone.identity.auth.persistence.account.jpa;

import com.lilamaris.capstone.identity.auth.application.account.port.out.criteria.FederatedProviderLookupCriteria;
import com.lilamaris.capstone.identity.auth.application.account.port.out.criteria.FederatedUserLookupCriteria;
import com.lilamaris.capstone.identity.auth.domain.account.FederatedAccount;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        JpaFederatedAccountPersistenceAdapter.class,
        JpaUserPersistenceAdapter.class
})
@Tag("unit")
@DisplayName("JpaFederatedAccountPersistenceAdapter JPA 테스트")
class JpaFederatedAccountPersistenceAdapterTest {
    private static final Instant CREATED_AT = Instant.parse("2026-01-01T00:00:00Z");

    @Autowired
    private JpaFederatedAccountPersistenceAdapter adapter;

    @Autowired
    private JpaUserPersistenceAdapter userAdapter;

    @Autowired
    private EntityManager entityManager;

    @Nested
    @DisplayName("federated 계정 저장")
    class SaveTest {
        @Test
        @DisplayName("federated 계정을 저장하고 식별자, 조회 조건, 사용자 식별자로 조회한다")
        void save_federated_account_and_find_by_id_criteria_and_user_id() {
            User user = userAdapter.save(User.of("tester", CREATED_AT));
            FederatedAccount saved = adapter.save(FederatedAccount.of(
                    user,
                    "google",
                    "google-user-1",
                    CREATED_AT
            ));
            flushAndClear();

            assertThat(adapter.existsByCriteria(new FederatedProviderLookupCriteria("google", "google-user-1")))
                    .isTrue();
            assertThat(adapter.findById(saved.getId()))
                    .hasValueSatisfying(found -> assertFederatedAccount(found, saved.getId(), user.getId()));
            assertThat(adapter.findByCriteria(new FederatedUserLookupCriteria(user.getId(), "google")))
                    .hasValueSatisfying(found -> assertFederatedAccount(found, saved.getId(), user.getId()));
            assertThat(adapter.findByUserId(user.getId()))
                    .singleElement()
                    .satisfies(found -> assertFederatedAccount(found, saved.getId(), user.getId()));
        }
    }

    @Nested
    @DisplayName("federated 계정 목록 조회")
    class ListTest {
        @Test
        @DisplayName("사용자 식별자로 해당 사용자의 federated 계정 목록을 조회한다")
        void find_federated_accounts_by_user_id() {
            User firstUser = userAdapter.save(User.of("tester", CREATED_AT));
            User secondUser = userAdapter.save(User.of("other", CREATED_AT));
            FederatedAccount google = adapter.save(FederatedAccount.of(
                    firstUser,
                    "google",
                    "google-user-1",
                    CREATED_AT
            ));
            FederatedAccount github = adapter.save(FederatedAccount.of(
                    firstUser,
                    "github",
                    "github-user-1",
                    CREATED_AT
            ));
            adapter.save(FederatedAccount.of(
                    secondUser,
                    "google",
                    "google-user-2",
                    CREATED_AT
            ));
            flushAndClear();

            assertThat(adapter.findByUserId(firstUser.getId()))
                    .extracting(FederatedAccount::getId)
                    .containsExactlyInAnyOrder(google.getId(), github.getId());
        }
    }

    @Nested
    @DisplayName("federated 계정 삭제")
    class DeleteTest {
        @Test
        @DisplayName("federated 계정을 삭제한다")
        void delete_federated_account() {
            User user = userAdapter.save(User.of("tester", CREATED_AT));
            FederatedAccount saved = adapter.save(FederatedAccount.of(
                    user,
                    "google",
                    "google-user-1",
                    CREATED_AT
            ));
            flushAndClear();

            adapter.delete(adapter.findById(saved.getId()).orElseThrow());
            flushAndClear();

            assertThat(adapter.existsByCriteria(new FederatedProviderLookupCriteria("google", "google-user-1")))
                    .isFalse();
            assertThat(adapter.findById(saved.getId())).isEmpty();
            assertThat(adapter.findByCriteria(new FederatedUserLookupCriteria(user.getId(), "google"))).isEmpty();
            assertThat(adapter.findByUserId(user.getId())).isEmpty();
        }
    }

    private static void assertFederatedAccount(FederatedAccount account, UUID accountId, UUID userId) {
        assertThat(account.getId()).isEqualTo(accountId);
        assertThat(account.getUser().getId()).isEqualTo(userId);
        assertThat(account.getRegistrationId()).isEqualTo("google");
        assertThat(account.getProviderUserId()).isEqualTo("google-user-1");
        assertThat(account.getCreatedAt()).isEqualTo(CREATED_AT);
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
