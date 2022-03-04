package usecase.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static usecase.auth.Pbkdf2PasswordHash.HashedPassword;

class Pbkdf2PasswordHashTest {
    @Test
    void testEquals(){
        Pbkdf2PasswordHash pbkdf2PasswordHash = new Pbkdf2PasswordHash();
        HashedPassword hp = pbkdf2PasswordHash.generate("pass");
        assertEquals(hp,hp);
        assertNotEquals(hp,null);
        assertNotEquals(hp,new Object());
        HashedPassword hpDifferent = pbkdf2PasswordHash.generate("passDifferent");
        assertNotEquals(hp,hpDifferent);
        HashedPassword hpSame = pbkdf2PasswordHash.generate("pass",hp.getSalt());
        assertEquals(hp,hpSame);
    }
}