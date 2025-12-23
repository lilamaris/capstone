package com.lilamaris.capstone.adapter.in.security.authn.oidc.vendor;

import com.lilamaris.capstone.adapter.in.security.authn.oidc.NormalizedProfile;
import com.lilamaris.capstone.adapter.in.security.authn.oidc.OidcProfileMapper;
import com.lilamaris.capstone.domain.user.Provider;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class NaverProfileMapper implements OidcProfileMapper {
    private final RestClient restClient = RestClient.create();

    @Override
    public NormalizedProfile map(OidcUser oidcUser, String registrationId) {
        var accessToken = (String) oidcUser.getAttribute("access_token");

        Map<String, Object> additionalUserInfo;

        Map<String, Object> response = restClient.get()
                .uri("https://openapi.naver.com/v1/nid/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        additionalUserInfo = (Map<String, Object>) response.get("response");

        var providerId = (String) additionalUserInfo.get("id");
        var email = (String) additionalUserInfo.get("email");
        var displayName = (String) additionalUserInfo.get("name");
        var attributes = additionalUserInfo;
        var idToken = oidcUser.getIdToken();
        var userInfo = oidcUser.getUserInfo();

        return NormalizedProfile.builder()
                .provider(Provider.NAVER)
                .providerId(providerId)
                .email(email)
                .displayName(displayName)
                .attributes(attributes)
                .idToken(idToken)
                .userInfo(userInfo)
                .build();
    }

    @Override
    public boolean supports(String registrationId) {
        return registrationId.equals("naver");
    }
}
