package service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

/**
 *
 * Classe le cui istanze rappresentano password criptate e il relativo salt.
 * Questa classe fornisce i metodi statici appositi per generare password criptate a partire da una stringa in chiaro
 * e un salt specificato o casuale.
 *
 */
public class HashedPassword {
    private byte[] password;
    private byte[] salt;

    private static byte[] generateSalt(){
        SecureRandom ss = new SecureRandom();
        byte[] salt = new byte[16];
        ss.nextBytes(salt);
        return salt;
    }

    /**
     * Genera un hash a partire dalla stringa specificata e da un salt generato casualmente
     * @param password La password da criptare
     * @return Un'istanza di HashedPassword appena inizializzata con la password criptata e il relativo salt generato
     */
    public static HashedPassword hash(String password){
        return hash(password, generateSalt());
    }


    /**
     * Genera un hash a partire dalla stringa e dal salt specificati
     * @param password La password da criptare
     * @param salt Il salt da aggiungere alla password prima di criptare
     * @return Un'istanza di HashedPassword appena inizializzata con la password criptata e il relativo salt generato
     */
    public static HashedPassword hash(String password, byte[] salt){
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();

            HashedPassword hp = new HashedPassword();
            hp.password = hash;
            hp.salt = salt;
            return hp;
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Controlla se se la stringa passata come parametro produce un hash identico all'oggetto su cui è chiamato il metodo.
     * Il salt usato per l'hashing è preso dall'oggetto su cui viene invocato il metodo.
     * @param password La password da verificare
     * @return true se gli hash coincidono, false altrimenti
     */
    public boolean check(String password){
        return this.equals(hash(password, this.salt));
    }

    /**
     * @param o L'oggetto da confrontare
     * @return True se il parametro è un'istanza di HashedPassword e se le password e il salt coincidono
     * con l'oggetto su cui è invocato il metodo, false altrimenti
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashedPassword that = (HashedPassword) o;
        return Arrays.equals(password, that.password) && Arrays.equals(salt, that.salt);
    }

    /* -- Getters and setters -- */

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }
}