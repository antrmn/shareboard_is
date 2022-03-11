package usecase.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import media.validation.Image;
import model.validation.EmailFormat;
import model.validation.PasswordFormat;
import model.validation.UniqueEmail;

import javax.validation.constraints.Size;
import java.io.BufferedInputStream;

/**
 * Classe DTO relativa alla modifica del profilo utente.
 */
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
