package com.lilamaris.capstone.adapter.in.security.authn.oidc.handler;

import com.lilamaris.capstone.adapter.in.security.util.ResponseWriter;
import com.lilamaris.capstone.adapter.in.security.SecurityUserDetails;
import com.lilamaris.capstone.adapter.in.security.util.SecurityUserDetailsMapper;
import com.lilamaris.capstone.application.port.in.AuthCommandUseCase;
import com.lilamaris.capstone.application.port.in.command.AuthCommand;
import com.lilamaris.capstone.domain.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OidcSuccessHandler implements AuthenticationSuccessHandler {
    private final AuthCommandUseCase authCommandUseCase;

    private final ResponseWriter writer;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        SecurityUserDetails principal = (SecurityUserDetails) authentication.getPrincipal();
        writer.sendToken(response, principal);
//        Authentication existAuth = SecurityContextHolder.getContext().getAuthentication();
//        SecurityUserDetails existUserDetails =
//                 (existAuth != null && existAuth.getPrincipal() instanceof SecurityUserDetails)
//                ? (SecurityUserDetails) existAuth.getPrincipal()
//                : null;
//
//        if (existUserDetails != null) {
//            handleAccountLink(response, existUserDetails, oidcDetails);
//        } else {
//            handleOidcLogin(response, oidcDetails);
//        }
    }

//    public void handleAccountLink(
//            HttpServletResponse response,
//            SecurityUserDetails existUser,
//            SecurityUserDetails oidcDetails
//    ) throws IOException {
//        var command = AuthCommand.LinkOidc.builder()
//                .userId(User.Id.from(existUser.getUserId()))
//                .provider(oidcDetails.getProvider())
//                .providerId(oidcDetails.getProviderId())
//                .displayName(oidcDetails.getDisplayName())
//                .build();
//        var linkResult = authCommandUseCase.linkAccount(command);
//
//        var userResult = linkResult.user();
//        var accountResult = linkResult.account();
//        var principal = SecurityUserDetailsMapper.from(userResult, accountResult);
//
//        writer.sendToken(response, principal);
//    }

    public void handleOidcLogin(
            HttpServletResponse response,
            SecurityUserDetails oidcDetails
    ) throws IOException {
        var command = AuthCommand.CreateOrLoginOidc.builder()
                .provider(oidcDetails.getProvider())
                .providerId(oidcDetails.getProviderId())
                .displayName(oidcDetails.getDisplayName())
                .build();
        var loginResult = authCommandUseCase.createOrLogin(command);

        var userResult = loginResult.user();
        var accountResult = loginResult.account();
        var principal = SecurityUserDetailsMapper.from(userResult, accountResult);

        writer.sendToken(response, principal);
    }
}
