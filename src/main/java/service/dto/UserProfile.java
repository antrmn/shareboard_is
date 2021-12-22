package service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Builder
@Getter
@Setter
public class UserProfile {

    private Integer id;
    private String username;
    private Instant creationDate;
    private String picture;
    private String description;
}
