package service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class UserProfile {

    private Integer id;
    private String username;
    private String email;
    private Instant creationDate;
    private String picture;
    private String description;
}
