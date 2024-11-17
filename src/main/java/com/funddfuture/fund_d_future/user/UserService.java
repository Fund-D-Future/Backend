package com.funddfuture.fund_d_future.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.funddfuture.fund_d_future.exception.*;
import java.security.Principal;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final MailerService mailerService;

    // get user details
    @Transactional(readOnly = true)
    public ResponseEntity<User> getUserDetails(Principal connectedUser) {
        return (ResponseEntity<User>) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
    }

    // src/main/java/com/funddfuture/fund_d_future/user/UserService.java
    @Transactional
    public ResponseEntity<User> updateUser( Principal connectedUser, UpdateUserRequest request) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setBio(request.getBio());
        user.setInstitution(request.getInstitution());
        user.setDegreeProgram(request.getDegreeProgram());
        user.setCourseOfStudy(request.getCourseOfStudy());
        user.setYearOfStudy(request.getYearOfStudy());
        user.setGrade(request.getGrade());
        user.setProof(request.getProof());
        user.setShortTermGoals(request.getShortTermGoals());
        user.setLongTermGoals(request.getLongTermGoals());
        user.setExtraCurricularActivities(request.getExtraCurricularActivities());
        user.setVolunteerWork(request.getVolunteerWork());
        user.setResidentCountry(request.getResidentCountry());
        user.setRole(request.getRole());

        return ResponseEntity.ok(repository.save(user));
    }

    public ResponseEntity<User> changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }

        // Checks if old password and new password are same
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new IllegalStateException("New password cannot be the same as the old password");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        // return a json response to let user know password was changed successfully
        return ResponseEntity.ok(repository.save(user));
    }

    public ResponseEntity<String> forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        String email = forgotPasswordRequest.getEmail();
        Optional<User> userOptional = repository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        User user = userOptional.get();
        String resetToken = generateResetToken();
        Date resetPasswordExpires = new Date(System.currentTimeMillis() + 3600000); // 1 hour
        mailerService.sendPasswordResetEmail(email, resetToken);

        user.setResetPasswordToken(resetToken);
        user.setResetPasswordExpires(resetPasswordExpires);
        repository.save(user);
        return ResponseEntity.ok("Reset Token successfully sent");

    }

    public ResponseEntity<String> resetPassword(String token, ResetPasswordRequest resetPasswordRequest) throws BadRequestException {
        String email = resetPasswordRequest.getEmail();
        String newPassword = resetPasswordRequest.getPassword();

        Optional<User> userOptional = repository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        User user = userOptional.get();
        if (!user.getResetPasswordToken().equals(token)) {
            throw new UnauthorizedException("Invalid reset token");
        }
        if (user.getResetPasswordExpires().before(new Date())) {
            throw new UnauthorizedException("Reset token expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setResetPasswordExpires(null);
        repository.save(user);
        return ResponseEntity.ok("Reset Password successful");

    }

    private String generateResetToken() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().substring(0, 6);
    }

    public ResponseEntity<User> createTransactionPin(String pin, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        user.setTransactionPin(passwordEncoder.encode(pin));
        ;
        return ResponseEntity.ok(repository.save(user));
    }

    public ResponseEntity<User> changeTransactionPin(String oldPin, String newPin, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        if (!passwordEncoder.matches(oldPin, user.getTransactionPin())) {
            throw new IllegalStateException("Incorrect old transaction pin");
        }
        user.setTransactionPin(passwordEncoder.encode(newPin));
        ;
        return ResponseEntity.ok(repository.save(user));
    }
}