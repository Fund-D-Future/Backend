package com.funddfuture.fund_d_future;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@RestController
public class FundDFutureApplication {

	public static void main(String[] args) {
		SpringApplication.run(FundDFutureApplication.class, args);
	}

	@GetMapping
	public String helloWorld() {
		return "Hello, BrainTrust!";
	}

	@GetMapping("/data")
	public List<String> getData() {
		// Fetch data from your database
		return Arrays.asList("Data 1", "Data 2", "Data 3");
	}

}
