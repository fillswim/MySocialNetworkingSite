package com.fillolej.mysns;

import com.fillolej.mysns.integration.initializer.Postgres;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(initializers = {
        Postgres.Initializer.class
})
class MySocialNetworkingSiteApplicationTests {

    // Start Testcontainer
    @BeforeAll
    static void startContainer() {
        Postgres.container.start();
    }

    @Test
    void contextLoads() {
    }

}
