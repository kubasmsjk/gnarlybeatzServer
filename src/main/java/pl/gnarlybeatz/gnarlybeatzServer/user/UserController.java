package pl.gnarlybeatz.gnarlybeatzServer.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @CrossOrigin(origins = "*")
    @PostMapping("/edit")
    public ResponseEntity<EditUserResponse> editUser(
            @RequestBody EditUserRequest request
    ) {
        return ResponseEntity.ok(userService.editUser(request));
    }
}
