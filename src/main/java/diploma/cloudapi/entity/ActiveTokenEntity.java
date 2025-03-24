package diploma.cloudapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "active_tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActiveTokenEntity implements Serializable {

    @Id
    @Column(name = "token")
    private String token;

    @Column(name = "user_name")
    private String username;

    @Column(name = "expires_at")
    private Instant expiresAt;
}
