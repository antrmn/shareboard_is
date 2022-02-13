package service.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

public class ImageValidator implements ConstraintValidator<Image, InputStream> {

    @Override
    public void initialize(Image constraintAnnotation) {

    }

    @Override
    public boolean isValid(InputStream value, ConstraintValidatorContext context) {
        String mime;

        if (value == null) return true;
        try {
            mime = URLConnection.guessContentTypeFromStream(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return mime != null && mime.startsWith("image/");
    }
}
