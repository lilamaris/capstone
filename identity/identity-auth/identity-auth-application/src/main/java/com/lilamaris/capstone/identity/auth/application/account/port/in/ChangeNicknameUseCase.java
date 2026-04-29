package com.lilamaris.capstone.identity.auth.application.account.port.in;

import com.lilamaris.capstone.identity.auth.application.account.port.in.command.ChangeNicknameCommand;
import com.lilamaris.capstone.identity.auth.application.account.port.in.result.UserResult;

public interface ChangeNicknameUseCase {
    UserResult change(ChangeNicknameCommand command);
}
