package util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static util.Pbkdf2PasswordHashImpl.HashedPassword;

class Pbkdf2PasswordHashImplTest {
    @Test
    void testEquals(){
        Pbkdf2PasswordHashImpl pbkdf2PasswordHash = new Pbkdf2PasswordHashImpl();
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