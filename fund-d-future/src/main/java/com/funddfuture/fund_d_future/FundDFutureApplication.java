package com.funddfuture.fund_d_future;

import com.funddfuture.fund_d_future.auth.AuthenticationService;
import com.funddfuture.fund_d_future.auth.RegisterRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import static com.funddfuture.fund_d_future.user.Role.ADMIN;
import static com.funddfuture.fund_d_future.user.Role.FUNDER;
import static com.funddfuture.fund_d_future.user.Role.USER;


@SpringBootApplication
@RestController
public class FundDFutureApplication {

	public static void main(String[] args) {
		SpringApplication.run(FundDFutureApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service
	) {
		return args -> {
			var admin = RegisterRequest.builder()
					.firstname("Admin")
					.lastname("Admin")
					.email("admin@mail.com")
					.password("password")
					.confirmPassword("password")
					.role(ADMIN)
					.build();
			System.out.println("Admin token: " + service.register(admin).getAccessToken());

			var funder = RegisterRequest.builder()
					.firstname("Funder")
					.lastname("Funder")
					.email("funder@mail.com")
					.password("password")
					.confirmPassword("password")
					.role(FUNDER)
					.build();
			System.out.println("Funder token: " + service.register(funder).getAccessToken());

			var user = RegisterRequest.builder()
					.firstname("User")
					.lastname("User")
					.email("user@mail.com")
					.password("password")
					.confirmPassword("password")
					.role(USER)
					.build();
			System.out.println("User token: " + service.register(user).getAccessToken());

		};
	}

}
