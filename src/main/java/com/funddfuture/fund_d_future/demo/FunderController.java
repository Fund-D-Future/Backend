package com.funddfuture.fund_d_future.demo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/funding")
@Tag(name = "Funders")
public class FunderController {


    @Operation(
            description = "Get endpoint for funder",
            summary = "This is a summary for funds get endpoint",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }

    )
    @GetMapping
    public String get() {
        return "GET:: funding controller";
    }
    @PostMapping
    public String post() {
        return "POST:: funding controller";
    }
    @PutMapping
    public String put() {
        return "PUT:: funding controller";
    }
    @DeleteMapping
    public String delete() {
        return "DELETE:: funding controller";
    }
}
