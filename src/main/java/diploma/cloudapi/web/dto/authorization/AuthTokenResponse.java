package diploma.cloudapi.web.dto.authorization;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthTokenResponse {

    @JsonProperty(value = "auth-token")
    private String authToken;

}
