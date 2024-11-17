package com.funddfuture.fund_d_future.user;

import com.funddfuture.fund_d_future.exception.NotFoundException;
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

    // src/main/java/com/funddfuture/fund_d_future/user/UserController.java
    @PutMapping("/")
    public ResponseEntity<User> updateUser(
            Principal connectedUser,
            @RequestBody UpdateUserRequest request
    ) {
        User updatedUser = service.updateUser(connectedUser, request);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<ResponseEntity<String>> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        return ResponseEntity.ok(service.changePassword(request, connectedUser));
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
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
        return ResponseEntity.ok().body("Password reset successful.");
    }

    @PostMapping("/create-transaction-pin")
    public ResponseEntity<String> createTransactionPin(@RequestBody String pin, Principal connectedUser) {
        service.createTransactionPin(pin, connectedUser);
        return ResponseEntity.ok("Transaction pin created successfully");
    }

    @PatchMapping("/change-transaction-pin")
    public ResponseEntity<String> changeTransactionPin(@RequestBody ChangePinRequest request, Principal connectedUser) {
        service.changeTransactionPin(request.getOldPin(), request.getNewPin(), connectedUser);
        return ResponseEntity.ok("Transaction pin changed successfully");
    }


}