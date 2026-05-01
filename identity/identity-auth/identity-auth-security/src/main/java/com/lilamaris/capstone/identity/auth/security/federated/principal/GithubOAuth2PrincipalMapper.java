package com.lilamaris.capstone.identity.auth.security.federated.principal;

import com.lilamaris.capstone.identity.auth.security.federated.registry.FederatedPrincipalMapper;
import com.lilamaris.capstone.kernel.core.condition.Preconditions;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.regex.Pattern;

public class GithubOAuth2PrincipalMapper implements FederatedPrincipalMapper {
    private static final String FALLBACK_NICKNAME = "user";

    @Override
    public String key() {
        return "github";
    }

    @Override
    public FederatedPrincipal resolve(OAuth2User oAuth2User) {
        var attributes = oAuth2User.getAttributes();
        var authorities = oAuth2User.getAuthorities();

        var registrationId = key();
        var providerUserNickname = getNickname(attributes);
        var providerUserId = getProviderUserId(attributes);

        return new OAuth2FederatedPrincipal(authorities, attributes, registrationId, providerUserId, providerUserNickname);
    }

    private String getProviderUserId(Map<String, Object> attributes) {
        var id = attributes.get("id");
        if (id == null) throw new IllegalArgumentException("GitHub OAuth2 user id must not be null.");

        return Preconditions.requireNonBlank(String.valueOf(id), "providerUserId");
    }

    private String getNickname(Map<String, Object> attributes) {
        var nickname = (String) attributes.get("name");
        var email = (String) attributes.get("email");

        if (StringUtils.hasText(nickname)) return nickname;

        if (!StringUtils.hasText(email)) return FALLBACK_NICKNAME;

        var emailParts = email.split(Pattern.quote("@"));

        if (emailParts.length != 2 || emailParts[0].isBlank()) return FALLBACK_NICKNAME;

        return emailParts[0];
    }
}
