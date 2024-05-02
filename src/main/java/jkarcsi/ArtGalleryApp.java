package jkarcsi;

import static jkarcsi.utils.helpers.SampleData.SAMPLE_USERS_PASSWORD;
import static jkarcsi.utils.helpers.SampleData.SAMPLE_USER_ONE;
import static jkarcsi.utils.helpers.SampleData.SAMPLE_USER_TWO;

import java.util.ArrayList;
import java.util.Collections;

import jkarcsi.dto.user.AppUser;
import jkarcsi.dto.user.AppUserRole;
import jkarcsi.repository.UserRepository;
import jkarcsi.service.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ArtGalleryApp {

    public static void main(String[] args) {
        SpringApplication.run(ArtGalleryApp.class, args);
    }

    @Bean
    public ApplicationRunner run(final UserService userService, final UserRepository userRepository) {
        return (ApplicationArguments args) -> {
            if (!(userRepository.existsByUsername(SAMPLE_USER_ONE) && userRepository.existsByUsername(
                    SAMPLE_USER_TWO))) {
                AppUser admin = new AppUser().setUsername(SAMPLE_USER_ONE).setPassword(SAMPLE_USERS_PASSWORD)
                        .setAppUserRoles(new ArrayList<>(Collections.singletonList(AppUserRole.ROLE_ADMIN)));
                AppUser client = new AppUser().setUsername(SAMPLE_USER_TWO).setPassword(SAMPLE_USERS_PASSWORD)
                        .setAppUserRoles(new ArrayList<>(Collections.singletonList(AppUserRole.ROLE_CLIENT)));
                userService.signUp(admin);
                userService.signUp(client);
            }
        };

    }

}
