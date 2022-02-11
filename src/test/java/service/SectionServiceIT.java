package service;

import org.apache.bval.cdi.BValInterceptor;
import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import persistence.model.Section;
import persistence.repo.*;
import rocks.limburg.cdimock.CdiMock;
import service.dto.CurrentUser;
import service.dto.SectionPage;

import javax.inject.Inject;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@Classes(cdi = true,
        value={SectionService.class,
                BinaryContentRepository.class,
                GenericRepository.class,
                SectionRepository.class},
        cdiInterceptors = BValInterceptor.class,
        cdiStereotypes = CdiMock.class)
@Disabled
public class SectionServiceIT extends PersistenceIT {
    @Inject private SectionService service;
    @Inject private GenericRepository repository;
    @Inject private BinaryContentRepository bcRepo;
    @Mock private CurrentUser currentUser;

    private List<Section> sections;

    @BeforeAll
    public void populate() throws Exception {
        doTransactional(() -> {
            sections = IntStream.range(1, 4).mapToObj(n -> {
                Section section = new Section();
                section.setName("section" + n);
                section.setBanner("banner" + n + ".png");
                section.setPicture("picture" + n + ".png");
                section.setDescription("description" + n);
                return section;
            }).map(repository::insert).collect(Collectors.toList());
        });
    }

    @Test
    void successfulShowSections() throws Exception {
        doThenRollback((em) -> {
            when(currentUser.getId()).thenReturn(0);
            when(currentUser.isLoggedIn()).thenReturn(false);
            List<SectionPage> sections2 = service.showSections();
            for(int i = 0; i < sections.size(); i++){
                assertEquals(sections.get(i).getId(), sections2.get(i).getId());
            }
            assertEquals(sections2.size(), sections.size());
        });
    }



}
