package pl.gnarlybeatz.gnarlybeatzServer.emial;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public String send(@RequestBody EmailRequest request) {
        return emailService.send(request);
    }
}