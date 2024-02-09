package pl.gnarlybeatz.gnarlybeatzServer.emial;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.gnarlybeatz.gnarlybeatzServer.validator.ObjectRequestInterface;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest implements ObjectRequestInterface {

    @Email(regexp = ".+[@].+[\\.].+", message = "This is not a valid email.")
    private String sender;
    @Size(min = 50, max = 100, message = "The message must consist of 50 to 100 characters.")
    private String msgBody;
    @Pattern(regexp = "^(Exclusive buy offer|Technical issues|Transaction issues|Other)$",
            message = "Invalid subject. Choose from: Exclusive buy offer, Technical issues, Transaction issues, Other.")
    private String subject;

    public void setSender(String sender) {
        this.sender = sender.trim();
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody.trim();
    }

    public void setSubject(String subject) {
        this.subject = subject.trim();
    }
}

