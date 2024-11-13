package com.funddfuture.fund_d_future.user;

import com.funddfuture.fund_d_future.exception.BadRequestException;
import com.funddfuture.fund_d_future.exception.NotFoundException;
import com.funddfuture.fund_d_future.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    // get user details
    @GetMapping("/user-details")
    public ResponseEntity<User> getUserDetails(Principal connectedUser) {
        User userDetails = service.getUserDetails(connectedUser);
        return ResponseEntity.ok(userDetails);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        try {
            service.changePassword(request, connectedUser);
        } catch (IllegalStateException e) {
            throw new BadRequestException(e.getMessage());
        }
        return ResponseEntity.ok("Password changed successfully");
    };

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        try {
            service.forgotPassword(forgotPasswordRequest);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
        return ResponseEntity.ok().body("Password reset link has been sent.");
    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("token") String token, @RequestBody ResetPasswordRequest resetPasswordRequest) {
        try {
            service.resetPassword(token, resetPasswordRequest);
        } catch (BadRequestException | NotFoundException | UnauthorizedException e) {
            throw e;
        }
        return ResponseEntity.ok().body("Password reset successful.");
    }
}