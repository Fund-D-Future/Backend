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
    public ResponseEntity<ResponseEntity<User>> getUserDetails(Principal connectedUser) {
        return ResponseEntity.ok(service.getUserDetails(connectedUser));
    }

    // src/main/java/com/funddfuture/fund_d_future/user/UserController.java
    @PutMapping("/")
    public ResponseEntity<User> updateUser(
            Principal connectedUser,
            @RequestBody UpdateUserRequest request
    ) {
        return ResponseEntity.ok(service.updateUser(connectedUser, request).getBody());
    }

    @PatchMapping("/change-password")
    public ResponseEntity<ResponseEntity<User>> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        return ResponseEntity.ok().body(service.changePassword(request, connectedUser));
    };

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        return ResponseEntity.ok().body(service.forgotPassword(forgotPasswordRequest));
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
    public ResponseEntity<ResponseEntity<User>> createTransactionPin(@RequestBody String pin, Principal connectedUser) {
        return ResponseEntity.ok(service.createTransactionPin(pin, connectedUser));
    }

    @PatchMapping("/change-transaction-pin")
    public ResponseEntity<ResponseEntity<User>> changeTransactionPin(@RequestBody ChangePinRequest request, Principal connectedUser) {
        return ResponseEntity.ok().body(service.changeTransactionPin(request.getOldPin(), request.getNewPin(), connectedUser));
    }


}