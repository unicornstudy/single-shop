package com.unicornstudy.singleshop.oauth2.dto;

import java.util.Map;

public interface OAuthInterface {
    OAuthAttributes create(String userNameAttributeName, Map<String, Object> attributes);
}
