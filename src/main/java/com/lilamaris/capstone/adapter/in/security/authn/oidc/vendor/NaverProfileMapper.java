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
                oidcUser.getIdToken(),
                oidcUser.getUserInfo(),
                oidcUser.getClaims()
        );
    }

    @Override
    public boolean supports(String registrationId) {
        return registrationId.equals("naver");
    }
}
