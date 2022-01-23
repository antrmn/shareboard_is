package service.dto;

import lombok.*;

import javax.enterprise.inject.Alternative;
import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Alternative
@ToString
public class CurrentUser {
    private int id;
    private String username;
    private boolean isAdmin;
    private Instant banDuration;
    @Builder.Default
    private boolean isLoggedIn = false;
}
