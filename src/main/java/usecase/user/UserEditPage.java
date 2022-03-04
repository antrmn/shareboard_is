package usecase.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import media.validator.Image;
import usecase.user.validator.EmailFormat;
import usecase.user.validator.PasswordFormat;
import usecase.user.validator.UniqueEmail;

import javax.validation.constraints.Size;
import java.io.BufferedInputStream;

@Builder @Getter @Data @AllArgsConstructor
public class UserEditPage {

    @Size(max=255)
    private String description;

    @EmailFormat
    @UniqueEmail
    private String email;

    @Image
    private BufferedInputStream picture;

    @PasswordFormat
    private String password;
}
