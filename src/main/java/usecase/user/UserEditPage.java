package usecase.user;

import domain.validation.EmailFormat;
import domain.validation.PasswordFormat;
import domain.validation.UniqueEmail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import media.validation.Image;

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
