package com.lilamaris.capstone.identity.auth.application.account;

import com.lilamaris.capstone.identity.auth.application.account.port.out.UserReader;
import com.lilamaris.capstone.identity.auth.application.account.port.out.UserStore;
import com.lilamaris.capstone.identity.auth.application.account.service.UserService;
import com.lilamaris.capstone.identity.auth.application.shared.exception.IdentityAuthApplicationErrorCode;
import com.lilamaris.capstone.identity.auth.domain.account.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
@DisplayName("User 유스케이스 흐름 테스트")
class UserUseCaseTest {
    @Mock
    UserReader reader;

    @Mock
    UserStore store;

    UserService service;

    @BeforeEach
    void setUp() {
        service = new UserService(reader, store);
    }

    @Nested
    @DisplayName("닉네임 변경")
    class ChangeNicknameTest {
        @Test
        @DisplayName("사용자를 조회하고 변경된 사용자를 저장한다")
        void find_user_and_save_updated_user() {
            when(reader.findById(AccountUseCaseTestSupport.USER_ID))
                    .thenReturn(Optional.of(AccountUseCaseTestSupport.user()));

            service.change(AccountUseCaseTestSupport.changeNicknameCommand());

            verify(reader).findById(AccountUseCaseTestSupport.USER_ID);
            verify(store).save(any(User.class));
        }

        @Test
        @DisplayName("닉네임을 변경할 사용자가 없으면 예외")
        void throw_exception_when_user_not_found() {
            when(reader.findById(AccountUseCaseTestSupport.USER_ID)).thenReturn(Optional.empty());

            AccountUseCaseTestSupport.assertApplicationError(
                    () -> service.change(AccountUseCaseTestSupport.changeNicknameCommand()),
                    IdentityAuthApplicationErrorCode.USER_NOT_FOUND
            );
        }
    }
}
