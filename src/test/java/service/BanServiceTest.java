package service;

import org.apache.bval.cdi.BValInterceptor;
import org.apache.openejb.testing.Classes;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import persistence.model.Ban;
import persistence.model.User;
import persistence.repo.GenericRepository;
import rocks.limburg.cdimock.CdiMock;
import service.dto.BanDTO;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Classes(cdi = true,
        value={BanService.class},
        cdiInterceptors = BValInterceptor.class,
        cdiStereotypes = CdiMock.class)
public class BanServiceTest extends ServiceTest{

    @Mock GenericRepository genericRepository;
    @Inject private BanService service;

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulAddBanWithFutureDate(int id){
        int year = LocalDate.now().getYear()+1;
        Instant data = LocalDate.of(year, 2, 8).atStartOfDay().toInstant(ZoneOffset.UTC);
        User user = new User();
        user.setId(1);
        when(genericRepository.findById(User.class,id)).thenReturn(user);
        Ban ban = new Ban();
        ban.setId(id);
        ban.setEndTime(data);
        ban.setUser(user);
        when(genericRepository.insert(any())).thenReturn(ban);
        Ban ban2 = service.addBan(data,id);
        assertEquals(ban,ban2);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void failAddBanWithPastDate(int id){
        int year = LocalDate.now().getYear()-1;
        Instant data = LocalDate.of(year, 2, 8).atStartOfDay().toInstant(ZoneOffset.UTC);
        assertThrows(ConstraintViolationException.class,() -> service.addBan(data,id));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -4, -30})
    void failAddBanWithWrongId(int id){
        int year = LocalDate.now().getYear()+1;
        Instant data = LocalDate.of(year, 2, 8).atStartOfDay().toInstant(ZoneOffset.UTC);
        when(genericRepository.findById(User.class,id)).thenReturn(null);
        assertThrows(ConstraintViolationException.class,() -> service.addBan(data,id));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 4, 50})
    void successfulRetrieveUserBan(int id){
        int year = LocalDate.now().getYear()+1;
        Instant data = LocalDate.of(year, 2, 8).atStartOfDay().toInstant(ZoneOffset.UTC);
        User user = spy(User.class);
        user.setId(id);
        when(genericRepository.findById(User.class,id)).thenReturn(user);
        List<Ban> bans = new ArrayList<>();
        Ban ban = new Ban();
        ban.setEndTime(data);
        ban.setUser(user);
        bans.add(ban);
        doReturn(bans).when(user).getBans();
        List<BanDTO> bansDTO = service.retrieveUserBan(id);
        assertTrue(bansDTO != null && bansDTO.size() == 1);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -4, -50})
    void failRetrieveUserBanWithWrongId(int id){
        when(genericRepository.findById(User.class,id)).thenReturn(null);
        assertThrows(ConstraintViolationException.class,() -> service.retrieveUserBan(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 7, 35})
    void successfulRemoveBan(int id){
        int year = LocalDate.now().getYear()+1;
        Instant data = LocalDate.of(year, 2, 8).atStartOfDay().toInstant(ZoneOffset.UTC);
        User user = new User();
        user.setId(1);
        when(genericRepository.findById(User.class,id)).thenReturn(user);
        Ban ban = new Ban();
        ban.setId(id);
        ban.setEndTime(data);
        ban.setUser(user);
        when(genericRepository.findById(Ban.class,id)).thenReturn(ban);
        assertDoesNotThrow(() -> service.removeBan(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {-2, -7, -35})
    void failRemoveBanWithWrongId(int id){
        when(genericRepository.findById(Ban.class,id)).thenReturn(null);
        assertThrows(ConstraintViolationException.class,() -> service.removeBan(id));
    }
}
