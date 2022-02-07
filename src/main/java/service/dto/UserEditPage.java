package service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import service.validation.Image;

import javax.validation.constraints.Email;
import java.io.BufferedInputStream;

@Builder @Getter @Data @AllArgsConstructor
public class UserEditPage {
    //todo: validation annotations

    private int userId;
    private String description;
    @Email private String email;
    @Image private BufferedInputStream picture;
    private String password;
}
