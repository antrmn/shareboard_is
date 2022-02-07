package util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

public class Pbkdf2PasswordHashImpl implements Serializable {
    public HashedPassword generate(String password, byte[] salt){
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();

            return new HashedPassword(hash, salt);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public HashedPassword generate(String password){
        return generate(password, generateSalt());
    }

    public boolean verify(String password, byte[] hashedPassword, byte[] salt){
        return verify(password, new HashedPassword(hashedPassword, salt));
    }

    public boolean verify(String password, HashedPassword hashedPassword){
        return hashedPassword.equals(generate(password, hashedPassword.getSalt()));
    }

    private static byte[] generateSalt(){
        SecureRandom ss = new SecureRandom();
        byte[] salt = new byte[16];
        ss.nextBytes(salt);
        return salt;
    }

    public static class HashedPassword{
        private byte[] password;
        private byte[] salt;

        private HashedPassword(byte[] password, byte[] salt) {
            this.password = password;
            this.salt = salt;
        }

        public byte[] getPassword() {
            return password;
        }

        public byte[] getSalt() {
            return salt;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HashedPassword that = (HashedPassword) o;
            return Arrays.equals(password, that.password) && Arrays.equals(salt, that.salt);
        }
    }
}
