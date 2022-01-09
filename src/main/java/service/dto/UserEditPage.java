package service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserEditPage {
    private Integer id;
    private String description;
    private String email;
    private String picture;
    private byte[] password;
}
