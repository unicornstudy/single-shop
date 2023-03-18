package com.unicornstudy.singleshop.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicornstudy.singleshop.oauth2.dto.SessionUser;
import com.unicornstudy.singleshop.user.dto.UpdateAddressDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser(roles = "USER")
@ExtendWith(RestDocumentationExtension.class)
class UserControllerTest {

    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    private MockHttpSession session = new MockHttpSession();

    private final String name = "test";
    private final String email = "testEmail";
    private final String picture = "testPicture";
    private final String city = "testCity";
    private final String street = "testStreet";
    private final String zipcode = "testZipcode";

    private User user;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        setMvc(restDocumentation);
        setUser();
        session.setAttribute("user", new SessionUser(user));
    }

    @Test
    void 주소_수정_테스트() throws Exception {
        UpdateAddressDto updateAddressDto = new UpdateAddressDto(city, street, zipcode);

        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.ofNullable(user));

        mvc
                .perform(RestDocumentationRequestBuilders.put("/api/user").session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .with(csrf())
                        .content(toJson(updateAddressDto)))
                .andExpect(status().isOk())
                .andDo(document("주소 수정 기능",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("city").description("도시"),
                                fieldWithPath("street").description("도로명"),
                                fieldWithPath("zipcode").description("우편번호")
                        )));
    }

    private void setMvc(RestDocumentationContextProvider restDocumentation) {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    private void setUser() {
        user = User.builder()
                .id(1L)
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.USER)
                .build();
    }

    public static String toJson(Object obj){
        try{
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}