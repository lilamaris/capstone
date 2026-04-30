package com.lilamaris.capstone.identity.auth.application.account;

import com.lilamaris.capstone.identity.auth.application.account.port.in.command.*;
import com.lilamaris.capstone.identity.auth.application.account.port.in.query.ListFederatedAccountQuery;
import com.lilamaris.capstone.identity.auth.application.shared.exception.IdentityAuthApplicationErrorCode;
import com.lilamaris.capstone.identity.auth.application.shared.exception.IdentityAuthApplicationException;
import com.lilamaris.capstone.identity.auth.domain.account.CredentialAccount;
import com.lilamaris.capstone.identity.auth.domain.account.FederatedAccount;
import com.lilamaris.capstone.identity.auth.domain.account.User;
import com.lilamaris.capstone.kernel.testsupport.FixedClock;
import com.lilamaris.capstone.kernel.testsupport.generator.SequenceCounter;
import com.lilamaris.capstone.kernel.testsupport.generator.UuidGenerator;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public final class AccountUseCaseTestSupport {
    public static final Clock CLOCK = FixedClock.getFixed();
    public static final Instant NOW = CLOCK.instant();

    public static final UUID USER_ID = new UuidGenerator(new SequenceCounter(1)).generate();

    public static final String NICKNAME = "tester";
    public static final String UPDATED_NICKNAME = "updated-tester";
    public static final String EMAIL = "tester@example.com";
    public static final String RAW_PASSWORD = "raw-password";
    public static final String NEW_RAW_PASSWORD = "new-raw-password";
    public static final String PASSWORD_HASH = "{bcrypt}password-hash";
    public static final String NEW_PASSWORD_HASH = "{bcrypt}new-password-hash";
    public static final String REGISTRATION_ID = "google";
    public static final String PROVIDER_USER_ID = "google-user-1";

    private AccountUseCaseTestSupport() {
    }

    public static User user() {
        return User.of(NICKNAME, NOW);
    }

    public static CredentialAccount credentialAccount() {
        return CredentialAccount.of(user(), EMAIL, PASSWORD_HASH, NOW);
    }

    public static FederatedAccount federatedAccount() {
        return FederatedAccount.of(user(), REGISTRATION_ID, PROVIDER_USER_ID, NOW);
    }

    public static RegisterCredentialAccountCommand registerCredentialAccountCommand() {
        return new RegisterCredentialAccountCommand(NICKNAME, EMAIL, RAW_PASSWORD);
    }

    public static AuthenticateCredentialAccountCommand authenticateCredentialAccountCommand() {
        return new AuthenticateCredentialAccountCommand(EMAIL, RAW_PASSWORD);
    }

    public static ChangeCredentialAccountCommand changeCredentialAccountCommand() {
        return new ChangeCredentialAccountCommand(USER_ID, RAW_PASSWORD, NEW_RAW_PASSWORD);
    }

    public static AuthenticateFederatedAccountCommand authenticateFederatedAccountCommand() {
        return new AuthenticateFederatedAccountCommand(NICKNAME, REGISTRATION_ID, PROVIDER_USER_ID);
    }

    public static LinkFederatedAccountCommand linkFederatedAccountCommand() {
        return new LinkFederatedAccountCommand(USER_ID, REGISTRATION_ID, PROVIDER_USER_ID);
    }

    public static UnlinkFederatedAccountCommand unlinkFederatedAccountCommand() {
        return new UnlinkFederatedAccountCommand(USER_ID, REGISTRATION_ID);
    }

    public static ListFederatedAccountQuery listFederatedAccountCommand() {
        return new ListFederatedAccountQuery(USER_ID);
    }

    public static ChangeNicknameCommand changeNicknameCommand() {
        return new ChangeNicknameCommand(USER_ID, UPDATED_NICKNAME);
    }

    public static void assertApplicationError(
            Runnable action,
            IdentityAuthApplicationErrorCode errorCode
    ) {
        assertThatThrownBy(action::run)
                .isInstanceOfSatisfying(IdentityAuthApplicationException.class, exception ->
                        org.assertj.core.api.Assertions.assertThat(exception.getErrorCode()).isEqualTo(errorCode)
                );
    }
}
