package com.unicornstudy.singleshop.oauth2.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public enum OAuthAttributesFactory {
    KAKAO(KaKaoOAuthAttributes::new),
    NAVER(NaverOAuthAttributes::new),
    GOOGLE(GoogleOAuthAttributes::new);

    private final Supplier<OAuthInterface> supplier;
}
