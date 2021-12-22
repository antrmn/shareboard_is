package service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserShowUsers {

    private Integer id;
    private String username;
    private Boolean isAdmin;
}
