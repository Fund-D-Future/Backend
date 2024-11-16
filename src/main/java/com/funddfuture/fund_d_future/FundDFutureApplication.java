package com.funddfuture.fund_d_future;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.RestController;



@SpringBootApplication
@EnableJpaAuditing
@RestController
public class FundDFutureApplication {

	public static void main(String[] args) {
		SpringApplication.run(FundDFutureApplication.class, args);
	}

	}
