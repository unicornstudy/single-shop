package com.unicornstudy.singleshop.oauth2.dto;

import java.util.Map;

public class GoogleOAuthAttributes implements OAuthInterface {

    @Override
    public OAuthAttributes create(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
}
