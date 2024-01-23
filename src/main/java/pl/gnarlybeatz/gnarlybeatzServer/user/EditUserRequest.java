package pl.gnarlybeatz.gnarlybeatzServer.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.gnarlybeatz.gnarlybeatzServer.auth.AuthenticateRequest;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditUserRequest implements AuthenticateRequest {

    @Size(min = 4, max = 20, message = "The user name must consist of 4 to 20 characters.")
    private String username;
    @Email(regexp = ".+[@].+[\\.].+", message = "This is not a valid email.")
    private String currentEmail;
    @Email(regexp = ".+[@].+[\\.].+", message = "This is not a valid email.")
    private String newEmail;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+{}\\[\\]:;<>,.?~\\\\/-])(?=\\S+$).{8,}$", message = "The password must contain at least 8 characters, one lowercase letter, one uppercase letter, one number, no whitespace and one special character.")
    private String currentPassword;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+{}\\[\\]:;<>,.?~\\\\/-])(?=\\S+$).{8,}$", message = "The password must contain at least 8 characters, one lowercase letter, one uppercase letter, one number, no whitespace and one special character.")
    private String newPassword;

    public void setUsername(String username) {
        this.username = username.trim();
    }

    public void setCurrentEmail(String currentEmail) {
        this.currentEmail = currentEmail.trim();
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail.trim();
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword.trim();
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword.trim();
    }
}
