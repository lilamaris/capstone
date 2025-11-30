package com.lilamaris.capstone.adapter.in.security.authn.oidc.vendor;

import com.lilamaris.capstone.adapter.in.security.authn.oidc.NormalizedProfile;
import com.lilamaris.capstone.adapter.in.security.authn.oidc.ProviderProfileMapper;
import com.lilamaris.capstone.domain.user.Provider;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.parameters.P;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class NaverProfileMapper implements ProviderProfileMapper {
    private final RestClient restClient = RestClient.create();

    @Override
    public NormalizedProfile map(OidcUser oidcUser, OAuth2User oAuth2User, String registrationId) {
        var accessToken = (String) oAuth2User.getAttribute("access_token");

        Map<String, Object> response;

        Map<String, Object> userinfo = restClient.get()
            .uri("https://openapi.naver.com/v1/nid/me")
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .body(new ParameterizedTypeReference<>() {});

        response = (Map<String, Object>) userinfo.get("response");

        return new NormalizedProfile(
                Provider.NAVER,
                (String) response.get("id"),
                (String) response.get("email"),
                (String) response.get("name"),
                response,
                oidcUser != null ? oidcUser.getIdToken() : null,
                oidcUser != null ? oidcUser.getUserInfo() : null,
                oidcUser != null ? oidcUser.getClaims() : Map.of()
        );
    }

    @Override
    public boolean supports(String registrationId) {
        return registrationId.equals("naver");
    }
}
