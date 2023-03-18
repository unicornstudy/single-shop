package com.unicornstudy.singleshop.user;

import com.unicornstudy.singleshop.carts.exception.SessionExpiredException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private final String name = "test";
    private final String email = "testEmail";
    private final String picture = "testPicture";

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    @AfterEach
    void delete() {
        userRepository.deleteAll();
    }

    @Test
    void 유저_저장_테스트() {
        User foundUser = userRepository.findById(user.getId()).orElseThrow(SessionExpiredException::new);

        assertThat(foundUser).isNotNull();
    }

    @Test
    void 이메일로_조회_테스트() {
        User foundUser = userRepository.findByEmail(email).orElseThrow(() -> new SessionExpiredException());

        assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());
    }
}