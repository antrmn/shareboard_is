package service;

import org.apache.bval.cdi.BValInterceptor;
import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import persistence.model.Section;
import persistence.repo.BinaryContentRepository;
import persistence.repo.GenericRepository;
import persistence.repo.SectionRepository;
import rocks.limburg.cdimock.CdiMock;
import service.dto.SectionPage;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Classes(cdi = true,
        value={SectionService.class},
        cdiInterceptors = BValInterceptor.class,
        cdiStereotypes = CdiMock.class)
public class SectionServiceTest extends ServiceTest{

    @Mock GenericRepository genericRepository;
    @Mock private SectionRepository sectionRepo;
    @Mock private BinaryContentRepository bcRepo;
    @Inject private SectionService service;

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulDeleteSection(int id){
       Section section = new Section();
       section.setId(id);
       when(genericRepository.findById(Section.class,id)).thenReturn(section);
       assertDoesNotThrow(() -> service.delete(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -30})
    void failDeleteSectionWithWrongId(int id){
        when(genericRepository.findById(Section.class,id)).thenReturn(null);
        assertThrows(ConstraintViolationException.class, () -> service.delete(id));
    }

    @Test
    void succesfulNewSection() throws IOException {
        when(bcRepo.insert(any())).thenReturn("fileName");
        Section section = new Section();
        section.setId(1);
        when(genericRepository.insert(any())).thenReturn(section);
        SectionPage sectionPage = new SectionPage(1,"name","description","picture","banner",2,false);
        int id = service.newSection(sectionPage, null, null);
        assertDoesNotThrow(() -> bcRepo.insert(any()));
        assertEquals(1,id);
    }

    @Test
    void failNewSection() throws IOException {
        when(bcRepo.insert(any())).thenReturn("fileName");
        when(genericRepository.insert(any())).thenReturn(null);
        SectionPage sectionPage = new SectionPage(1,"name","description","picture","banner",2,false);
        assertThrows(NullPointerException.class,() -> service.newSection(sectionPage, null, null));
    }


    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulShowSection(int id){
        Section section = new Section();
        section.setId(id);
        section.setBanner("banner");
        section.setDescription("description");
        section.setPicture("picture");
        section.setName("name");
        when(genericRepository.findById(Section.class,id)).thenReturn(section);
        SectionPage sectionPage = service.showSection(id);
        assertNotNull(sectionPage);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void failShowSectionWithWrongId(int id){
        when(genericRepository.findById(Section.class,id)).thenReturn(null);
        assertThrows(ConstraintViolationException.class,() -> service.showSection(id));
    }

    @ParameterizedTest
    @ValueSource(strings = {"section1", "section2", "section3"})
    void successfulGetSection(String sectionName){
        Section section = new Section();
        section.setId(1);
        section.setBanner("banner");
        section.setDescription("description");
        section.setPicture("picture");
        section.setName(sectionName);
        when(sectionRepo.getByName(sectionName)).thenReturn(section);
        SectionPage sectionPage = service.getSection(sectionName);
        assertNotNull(sectionPage);
    }

    @ParameterizedTest
    @ValueSource(strings = {"notSection1", "notSection2", "notSection3"})
    void failGetSectionWithWrongName(String wrongName){
        when(sectionRepo.getByName(wrongName)).thenReturn(null);
        assertThrows(ConstraintViolationException.class,() -> service.getSection(wrongName));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulEditSection(int id) throws IOException {
        SectionPage sectionPage = new SectionPage(1,"name","description","picture","banner",2,false);
        Section section = new Section();
        section.setId(id);
        when(genericRepository.findById(Section.class,id)).thenReturn(section);
        when(bcRepo.insert(any())).thenReturn("fileName");
        assertDoesNotThrow(() -> service.editSection(sectionPage,id,null,null));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -30})
    void failEditSectionWithWrongID(int id) throws IOException {
        SectionPage sectionPage = new SectionPage(1,"name","description","picture","banner",2,false);
        when(genericRepository.findById(Section.class,id)).thenReturn(null);
        assertThrows(ConstraintViolationException.class,() -> service.editSection(sectionPage,id,null,null));
    }

}
