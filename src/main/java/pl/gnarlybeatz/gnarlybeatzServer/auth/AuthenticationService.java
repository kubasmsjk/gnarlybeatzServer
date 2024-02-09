package pl.gnarlybeatz.gnarlybeatzServer.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.gnarlybeatz.gnarlybeatzServer.config.JwtService;
import pl.gnarlybeatz.gnarlybeatzServer.exceptions.UserExistException;
import pl.gnarlybeatz.gnarlybeatzServer.token.Token;
import pl.gnarlybeatz.gnarlybeatzServer.token.TokenRepository;
import pl.gnarlybeatz.gnarlybeatzServer.token.TokenType;
import pl.gnarlybeatz.gnarlybeatzServer.user.User;
import pl.gnarlybeatz.gnarlybeatzServer.user.UserRepository;
import pl.gnarlybeatz.gnarlybeatzServer.validator.ObjectRequestInterface;
import pl.gnarlybeatz.gnarlybeatzServer.validator.ObjectValidator;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static pl.gnarlybeatz.gnarlybeatzServer.user.Role.USER;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ObjectValidator<ObjectRequestInterface> userValidator;

    public AuthenticationResponse register(RegisterRequest request) {

        userValidator.validate(request);
        ifUserExistsThrowError(request.getEmail());

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isActive(true)
                .role(USER)
                .build();

        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);

        return AuthenticationResponse.builder()
                .id(user.getId())
                .username(request.getUsername())
                .email(user.getEmail())
                .role(String.valueOf(user.getRole()))
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void ifUserExistsThrowError(String email) {
        var user = getUserByEmailFromDb(email);
        if (user.isPresent()) {
            throw new UserExistException(Map.of("email", "User with this email already exists."));
        }
    }

    private Optional<User> getUserByEmailFromDb(String email) {
        return userRepository.findByEmail(email);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        userValidator.validate(request);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = getUserByEmailFromDb(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find your account."));

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .id(user.getId())
                .username(user.getName())
                .email(user.getEmail())
                .role(String.valueOf(user.getRole()))
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUserId(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
