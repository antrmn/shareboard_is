package service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
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
