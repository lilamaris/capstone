package com.lilamaris.capstone.application.port.in;

import com.lilamaris.capstone.application.port.in.command.AuthCommand;
import com.lilamaris.capstone.application.port.in.result.AuthResult;

import java.util.function.Function;

public interface AuthCommandUseCase {
    AuthResult.Login credentialLogin(String email, Function<String, Boolean> challengeFunction);
    AuthResult.Login createOrLogin(AuthCommand.CreateOrLoginOidc command);
    AuthResult.Link linkAccount(AuthCommand.LinkOidc command);
}
