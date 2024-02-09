package pl.gnarlybeatz.gnarlybeatzServer.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gnarlybeatz.gnarlybeatzServer.user.edit.EditUserRequest;
import pl.gnarlybeatz.gnarlybeatzServer.user.edit.EditUserResponse;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/edit")
    public ResponseEntity<EditUserResponse> editUser(
            @RequestBody EditUserRequest request
    ) {
        return ResponseEntity.ok(userService.editUser(request));
    }
}
