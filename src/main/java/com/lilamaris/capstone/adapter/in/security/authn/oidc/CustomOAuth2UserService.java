package com.lilamaris.capstone.adapter.in.security.authn.oidc;

import com.lilamaris.capstone.adapter.in.security.util.SecurityUserDetailsMapper;
import com.lilamaris.capstone.application.port.in.AuthCommandUseCase;
import com.lilamaris.capstone.application.port.in.command.AuthCommand;
import com.lilamaris.capstone.application.port.in.result.AuthResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final OAuth2ProfileMapperRegistry registry;
    private final AuthCommandUseCase authCommandUseCase;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);
        String registrationId = request.getClientRegistration().getRegistrationId();

        OAuth2ProfileMapper mapper = registry.findBy(registrationId);
        NormalizedProfile profile = mapper.map(oAuth2User, registrationId);

        var command = AuthCommand.CreateOrLoginOidc.builder()
                .provider(profile.provider())
                .providerId(profile.providerId())
                .displayName(profile.displayName())
                .build();
        AuthResult.Login result = authCommandUseCase.createOrLogin(command);

        return SecurityUserDetailsMapper.from(result.user(), result.account());
    }
}
