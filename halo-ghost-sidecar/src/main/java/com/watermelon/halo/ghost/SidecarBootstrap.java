package com.watermelon.halo.ghost;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SidecarBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(SidecarBootstrap.class, args);
    }

    @Bean
    public ApplicationRunner runner(LockManager lockManager) {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                String lockKey = "halo:ghost:leader";
                boolean acquired = lockManager.tryLock(lockKey, 30);
                if (!acquired) {
                    System.err.println("[halo-ghost-sidecar] failed to acquire lock -> exiting");
                    // Exit with code 1 to signal failure to start
                    System.exit(1);
                }
                System.out.println("[halo-ghost-sidecar] lock acquired, starting sidecar");
            }
        };
    }
}
