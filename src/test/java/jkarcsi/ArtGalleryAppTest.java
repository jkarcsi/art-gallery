package jkarcsi;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class ArtGalleryAppTest {
    @Test
    void contextLoads(ApplicationContext applicationContext) {
        assertThat(applicationContext).isNotNull();
    }
}