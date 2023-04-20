package com.unicornstudy.singleshop.user.domain.repository;

import com.unicornstudy.singleshop.config.TestSetting;
import com.unicornstudy.singleshop.delivery.domain.Address;
import com.unicornstudy.singleshop.exception.user.UserNotFoundException;
import com.unicornstudy.singleshop.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    private User user;
    private Address address;

    @BeforeEach
    void setUp() {
        address = TestSetting.setAddress();
        user = TestSetting.setUser(address);

        userRepository.save(user);
    }

    @DisplayName("회원 데이터 저장 테스트")
    @Test
    void save_user_test() {
        User newUser = TestSetting.setUser(address);

        userRepository.save(newUser);

        User foundUser = userRepository.findById(newUser.getId())
                .orElseThrow(UserNotFoundException::new);

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(newUser.getId());
    }

    @DisplayName("회원 이메일을 통한 회원 조회 테스트")
    @Test
    void findByEmail_test() {
        User foundUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(UserNotFoundException::new);

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());
    }

    @DisplayName("존재하지 않는 이메일로 회원 조회 예외 테스트")
    @Test
    void findByEmail_exception_test() {
        assertThat(userRepository.findByEmail("invalidUser")).isEmpty();
    }
}