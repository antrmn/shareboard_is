package service.dto;

import lombok.*;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserIdentityDTO {
    private int id;
    private String username;
    private boolean isAdmin;

    public UserIdentityDTO(int id, String username, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.isAdmin = isAdmin;
    }
}
