package com.unicornstudy.singleshop.oauth2.dto;

import java.util.Map;

public class NaverOAuthAttributes implements OAuthInterface {

    @Override
    public OAuthAttributes create(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        userNameAttributeName = "id";

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
}
