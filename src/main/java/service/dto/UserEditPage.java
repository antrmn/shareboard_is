package service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import service.validation.EmailFormat;
import service.validation.Image;
import service.validation.PasswordFormat;
import service.validation.UniqueEmail;

import javax.validation.constraints.Size;
import java.io.BufferedInputStream;

@Builder @Getter @Data @AllArgsConstructor
public class UserEditPage {

    @Size(max=255)
    private String description;

    @EmailFormat @UniqueEmail
    private String email;

    @Image
    private BufferedInputStream picture;

    @PasswordFormat
    private String password;
}
