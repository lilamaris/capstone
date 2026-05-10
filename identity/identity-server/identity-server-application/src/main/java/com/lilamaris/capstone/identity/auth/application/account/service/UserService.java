package com.lilamaris.capstone.identity.auth.application.account.service;

import com.lilamaris.capstone.identity.auth.application.account.port.in.ChangeNicknameUseCase;
import com.lilamaris.capstone.identity.auth.application.account.port.in.command.ChangeNicknameCommand;
import com.lilamaris.capstone.identity.auth.application.account.port.in.result.UserResult;
import com.lilamaris.capstone.identity.auth.application.account.port.out.UserReader;
import com.lilamaris.capstone.identity.auth.application.account.port.out.UserStore;
import com.lilamaris.capstone.identity.auth.application.shared.exception.IdentityAuthApplicationErrorCode;
import com.lilamaris.capstone.identity.auth.application.shared.exception.IdentityAuthApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements ChangeNicknameUseCase {

    private final UserReader reader;
    private final UserStore store;

    @Override
    @Transactional
    public UserResult change(ChangeNicknameCommand command) {
        var user = reader.findById(command.userId())
                .orElseThrow(() -> new IdentityAuthApplicationException(IdentityAuthApplicationErrorCode.USER_NOT_FOUND));

        user.updateNickname(command.nickname());
        store.save(user);

        return UserResult.from(user);
    }
}
