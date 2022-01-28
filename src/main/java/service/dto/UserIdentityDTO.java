package service.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserIdentityDTO {
    private int id;
    private String username;
    private boolean isAdmin;
}
