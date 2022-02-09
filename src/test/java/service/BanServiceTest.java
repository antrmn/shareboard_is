package service;

import org.apache.bval.cdi.BValInterceptor;
import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import persistence.repo.GenericRepository;
import rocks.limburg.cdimock.CdiMock;

import javax.inject.Inject;
import java.time.Instant;

@Classes(cdi = true,
        value={BanService.class},
        cdiInterceptors = BValInterceptor.class,
        cdiStereotypes = CdiMock.class)
public class BanServiceTest extends ServiceTest{

    @Mock GenericRepository genericRepository;
    @Inject BanService service;

    @Test
    void successAddBan(){
        service.addBan(Instant.now(),1);
    }
}
