package pl.gnarlybeatz.gnarlybeatzServer.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.gnarlybeatz.gnarlybeatzServer.auth.AuthenticateRequest;
import pl.gnarlybeatz.gnarlybeatzServer.exceptions.TheSameEmailException;
import pl.gnarlybeatz.gnarlybeatzServer.exceptions.TheSamePasswordException;
import pl.gnarlybeatz.gnarlybeatzServer.validator.ObjectValidator;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectValidator<AuthenticateRequest> userValidator;

    public EditUserResponse editUser(EditUserRequest request) {

        userValidator.validate(request);

        User currentUser = returnUserOrIfUserNotExistsThrowError(request.getCurrentEmail());
        String currentPassword = currentUser.getPassword();
        String requestCurrentPassword = request.getCurrentPassword();
        String newPassword = request.getNewPassword();
        String newEmail = request.getNewEmail();

        checkIsNewEmailEqualsCurrentEmail(newEmail, currentUser.getEmail());
        checkIsNewEmailInDb(newEmail);

        boolean matchesRequestCurrentPasswordToCurrentPassword = passwordEncoder.matches(requestCurrentPassword, currentPassword);
        boolean matches = checkIsNewPasswordEqualsCurrentPassword(matchesRequestCurrentPasswordToCurrentPassword, newPassword, currentPassword);

        if (matches || requestCurrentPassword.equals(newPassword)) {
            throw new TheSamePasswordException(Map.of("newPassword", "The new password cannot be the same as current password."));
        } else {
            currentUser.setUsername(request.getUsername());
            currentUser.setEmail(newEmail);
            currentUser.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(currentUser);
            return EditUserResponse.builder()
                    .username(currentUser.getName())
                    .email(currentUser.getEmail())
                    .build();
        }

    }
    private void checkIsNewEmailInDb(String newEmail) {
        if (userRepository.findByEmail(newEmail).isPresent()) {
            throw new TheSameEmailException(Map.of("newEmail", "This email is used."));
        }
    }

    private void checkIsNewEmailEqualsCurrentEmail(String newEmail, String currentEmail) {
        if (newEmail.equals(currentEmail)) {
            throw new TheSameEmailException(Map.of("newEmail", "The new email cannot be the same as current email."));
        }
    }

    private User returnUserOrIfUserNotExistsThrowError(String email) {
        var user = getUserByEmailFromDb(email);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UsernameNotFoundException("Couldn't find your account.");
        }
    }

    private boolean checkIsNewPasswordEqualsCurrentPassword(boolean matchesRequestCurrentPasswordToCurrentPassword, String newPassword, String currentPassword) {
        if (matchesRequestCurrentPasswordToCurrentPassword) {
            return passwordEncoder.matches(newPassword, currentPassword);
        } else {
            throw new TheSamePasswordException(Map.of("currentPassword", "Incorrect password."));
        }
    }

    private Optional<User> getUserByEmailFromDb(String email) {
        return userRepository.findByEmail(email);
    }
}
