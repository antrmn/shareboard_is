package service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedInputStream;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserEditPage {
    //todo: validation annotations

    private Integer id;
    private String description;
    private String email;
    private BufferedInputStream picture;
    private String password;
}
