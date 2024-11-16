package com.funddfuture.fund_d_future;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.RestController;



@SpringBootApplication
@EnableJpaAuditing
@ComponentScan(basePackages = {"com.funddfuture.fund_d_future"})
@RestController
public class FundDFutureApplication {

	public static void main(String[] args) {
		SpringApplication.run(FundDFutureApplication.class, args);
	}

	}
