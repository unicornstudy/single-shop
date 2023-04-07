package com.unicornstudy.singleshop.subscription.infrastructure.batch;

import com.unicornstudy.singleshop.config.TestSetting;
import com.unicornstudy.singleshop.subscription.domain.repository.SubscriptionRepository;
import com.unicornstudy.singleshop.user.domain.Role;
import com.unicornstudy.singleshop.user.domain.User;
import com.unicornstudy.singleshop.user.domain.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@Disabled
public class RegularPaymentJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    private User user;

    @Container
    private static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8")
            .withDatabaseName("unicornTest")
            .withUsername("TEST")
            .withPassword("12345");

    @BeforeEach
    public void setUp() {
        user = TestSetting.setUser(TestSetting.setAddress());
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        subscriptionRepository.deleteAll();
    }

    @Test
    @DisplayName("배치가 실행 되는지 확인하는 테스트")
    public void 배치_실행_테스트() throws Exception {
        JobExecution execution = jobLauncherTestUtils.launchJob();

        assertThat(execution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }

    @Test
    @DisplayName("배치 작업 중 정기 구독이 실행 되는지 확인하는 테스트")
    public void 정기_구독_테스트() throws Exception {
        user.updateRole(Role.SUBSCRIBER);
        userRepository.save(user);

        JobExecution execution = jobLauncherTestUtils.launchJob();

        User result = userRepository.findAll().get(0);
        assertThat(execution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        assertThat(result.getRole()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("배치 작업 중 정기 구독 취소가 실행 되는지 확인하는 테스트")
    public void 구독_취소_테스트() throws Exception {
        user.updateRole(Role.CANCEL_SUBSCRIBER);
        userRepository.save(user);

        JobExecution execution = jobLauncherTestUtils.launchJob();

        User result = userRepository.findAll().get(0);
        assertThat(execution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        assertThat(result.getRole()).isEqualTo(Role.USER);
    }
}