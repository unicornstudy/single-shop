package com.unicornstudy.singleshop.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class OAuthConfigTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void 카카오로그인_화면_테스트() throws Exception {
        mvc.perform(get("/oauth2/authorization/kakao"))
                .andDo(print())
                .andExpect(status().is(302));
    }

    @Test
    public void 카카오_인가_코드_받기(@Value("${spring.security.oauth2.client.registration.kakao.clientId}")String clientId, @Value("${spring.security.oauth2.client.registration.kakao.redirectUri}") String redirectUri) throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("response_type", "code");
        mvc.perform(get("https://kauth.kakao.com/oauth/authorize")
                        .params(params))
                            .andDo(print())
                            .andExpect(status().is(302));
    }

    @Test
    public void 카카오_엑세스_토큰_발급() throws Exception {
        String registrationId = "kakao";
        //String userNameAttributeName =;
        //OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
    }

    @Test
    public void 회원가입_테스트() throws Exception {

    }
}