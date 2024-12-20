package io.joework.malabaakapi;

import io.joework.malabaakapi.config.security.RSAKeysProperties;
import io.joework.malabaakapi.service.VerificationMailConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
@EnableConfigurationProperties(value = {
		RSAKeysProperties.class,
		VerificationMailConfigProperties.class
})
public class MalabaakApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MalabaakApiApplication.class, args);
	}

}
