package com.lilamaris.capstone.adapter.in.security.authn.oidc;

import com.lilamaris.capstone.adapter.in.security.SecurityUserDetailsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {
    private final ProviderProfileMapperRegistry registry;
    @Override
    public OidcUser loadUser(OidcUserRequest request) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(request);
        String registrationId = request.getClientRegistration().getRegistrationId();

        ProviderProfileMapper mapper = registry.findBy(registrationId);
        NormalizedProfile profile = mapper.map(oidcUser, oidcUser, registrationId);

        return SecurityUserDetailsMapper.fromProfile(profile);
    }
}
