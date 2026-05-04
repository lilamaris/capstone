package com.lilamaris.capstone.identity.auth.persistence.role.jpa;

import com.lilamaris.capstone.identity.auth.application.role.port.out.criteria.UserGrantRoleLookupCriteria;
import com.lilamaris.capstone.identity.auth.domain.role.UserGrantedRole;
import com.lilamaris.capstone.identity.core.role.CanonicalRole;
import com.lilamaris.capstone.kernel.core.namespace.SimpleApplicationNamespace;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaUserGrantedRolePersistenceAdapter.class)
@Tag("unit")
@DisplayName("JpaUserGrantedRolePersistenceAdapter JPA 테스트")
class JpaUserGrantedRolePersistenceAdapterTest {
    private static final UUID USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID OTHER_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private static final String NAMESPACE_NAME = "identity-auth";
    private static final Instant CREATED_AT = Instant.parse("2026-01-01T00:00:00Z");

    @Autowired
    private JpaUserGrantedRolePersistenceAdapter adapter;

    @Autowired
    private EntityManager entityManager;

    @Nested
    @DisplayName("사용자 부여 역할 저장")
    class SaveTest {
        @Test
        @DisplayName("사용자 부여 역할을 저장하고 식별자, 조회 조건, 사용자 식별자로 조회한다")
        void save_user_granted_role_and_find_by_id_criteria_and_user_id() {
            UserGrantedRole saved = adapter.save(userGrantedRole(USER_ID, NAMESPACE_NAME, CanonicalRole.USER));
            flushAndClear();

            assertThat(adapter.existsById(saved.getId())).isTrue();
            assertThat(adapter.existsByCriteria(criteria(USER_ID, NAMESPACE_NAME, CanonicalRole.USER))).isTrue();
            assertThat(adapter.findById(saved.getId()))
                    .hasValueSatisfying(found -> assertUserGrantedRole(found, saved.getId(), USER_ID, NAMESPACE_NAME, CanonicalRole.USER));
            assertThat(adapter.findByCriteria(criteria(USER_ID, NAMESPACE_NAME, CanonicalRole.USER)))
                    .hasValueSatisfying(found -> assertUserGrantedRole(found, saved.getId(), USER_ID, NAMESPACE_NAME, CanonicalRole.USER));
            assertThat(adapter.findByUserId(USER_ID))
                    .singleElement()
                    .satisfies(found -> assertUserGrantedRole(found, saved.getId(), USER_ID, NAMESPACE_NAME, CanonicalRole.USER));
        }

        @Test
        @DisplayName("사용자 부여 역할 목록을 저장한다")
        void save_user_granted_roles() {
            List<UserGrantedRole> saved = adapter.saveAll(List.of(
                    userGrantedRole(USER_ID, NAMESPACE_NAME, CanonicalRole.USER),
                    userGrantedRole(USER_ID, "timeline", CanonicalRole.GUEST)
            ));
            flushAndClear();

            assertThat(adapter.findByUserId(USER_ID))
                    .extracting(UserGrantedRole::getId)
                    .containsExactlyInAnyOrderElementsOf(saved.stream()
                            .map(UserGrantedRole::getId)
                            .toList());
        }
    }

    @Nested
    @DisplayName("사용자 부여 역할 목록 조회")
    class ListTest {
        @Test
        @DisplayName("사용자 식별자로 해당 사용자의 부여 역할 목록을 조회한다")
        void find_user_granted_roles_by_user_id() {
            UserGrantedRole user = adapter.save(userGrantedRole(USER_ID, NAMESPACE_NAME, CanonicalRole.USER));
            UserGrantedRole guest = adapter.save(userGrantedRole(USER_ID, "timeline", CanonicalRole.GUEST));
            adapter.save(userGrantedRole(OTHER_USER_ID, NAMESPACE_NAME, CanonicalRole.ADMIN));
            flushAndClear();

            assertThat(adapter.findByUserId(USER_ID))
                    .extracting(UserGrantedRole::getId)
                    .containsExactlyInAnyOrder(user.getId(), guest.getId());
        }
    }

    @Nested
    @DisplayName("사용자 부여 역할 삭제")
    class DeleteTest {
        @Test
        @DisplayName("사용자 부여 역할을 삭제한다")
        void delete_user_granted_role() {
            UserGrantedRole saved = adapter.save(userGrantedRole(USER_ID, NAMESPACE_NAME, CanonicalRole.USER));
            flushAndClear();

            adapter.delete(adapter.findById(saved.getId()).orElseThrow());
            flushAndClear();

            assertThat(adapter.existsById(saved.getId())).isFalse();
            assertThat(adapter.existsByCriteria(criteria(USER_ID, NAMESPACE_NAME, CanonicalRole.USER))).isFalse();
            assertThat(adapter.findById(saved.getId())).isEmpty();
            assertThat(adapter.findByCriteria(criteria(USER_ID, NAMESPACE_NAME, CanonicalRole.USER))).isEmpty();
            assertThat(adapter.findByUserId(USER_ID)).isEmpty();
        }
    }

    private static UserGrantedRole userGrantedRole(UUID userId, String namespaceName, CanonicalRole role) {
        return UserGrantedRole.of(
                userId,
                SimpleApplicationNamespace.of(namespaceName),
                role,
                CREATED_AT
        );
    }

    private static UserGrantRoleLookupCriteria criteria(UUID userId, String namespaceName, CanonicalRole role) {
        return UserGrantRoleLookupCriteria.of(
                userId,
                SimpleApplicationNamespace.of(namespaceName),
                role
        );
    }

    private static void assertUserGrantedRole(
            UserGrantedRole userGrantedRole,
            UUID roleId,
            UUID userId,
            String namespaceName,
            CanonicalRole role
    ) {
        assertThat(userGrantedRole.getId()).isEqualTo(roleId);
        assertThat(userGrantedRole.getUserId()).isEqualTo(userId);
        assertThat(userGrantedRole.getNamespace().name()).isEqualTo(namespaceName);
        assertThat(userGrantedRole.getRole()).isEqualTo(role);
        assertThat(userGrantedRole.getCreatedAt()).isEqualTo(CREATED_AT);
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
