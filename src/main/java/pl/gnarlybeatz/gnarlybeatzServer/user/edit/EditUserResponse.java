package pl.gnarlybeatz.gnarlybeatzServer.user.edit;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditUserResponse {

    @JsonProperty("username")
    private String username;
    @JsonProperty("email")
    private String email;

}
