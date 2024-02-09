package pl.gnarlybeatz.gnarlybeatzServer.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.gnarlybeatz.gnarlybeatzServer.validator.ObjectRequestInterface;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest implements ObjectRequestInterface {

    @Email(regexp = ".+[@].+[\\.].+", message = "This is not a valid email.")
    private String email;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+{}\\[\\]:;<>,.?~\\\\/-])(?=\\S+$).{8,}$", message = "The password must contain at least 8 characters, one lowercase letter, one uppercase letter, one number, no whitespace and one special character.")
    private String password;

    public void setEmail(String email) {
        this.email = email.trim();
    }

    public void setPassword(String password) {
        this.password = password.trim();
    }
}
