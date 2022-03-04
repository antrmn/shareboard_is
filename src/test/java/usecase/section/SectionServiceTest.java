package usecase.section;

import domain.entity.Follow;
import domain.entity.Section;
import domain.entity.User;
import domain.repository.GenericRepository;
import domain.repository.SectionRepository;
import media.MediaRepository;
import org.apache.bval.cdi.BValInterceptor;
import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import rocks.limburg.cdimock.CdiMock;
import usecase.ServiceTest;
import usecase.auth.CurrentUser;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Classes(cdi = true,
        value={SectionService.class},
        cdiInterceptors = BValInterceptor.class,
        cdiStereotypes = CdiMock.class)
public class SectionServiceTest extends ServiceTest {

    @Mock GenericRepository genericRepository;
    @Mock private SectionRepository sectionRepo;
    @Mock private MediaRepository bcRepo;
    @Mock private CurrentUser currentUser;
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
        int id = service.newSection("name","desc", null, null);
        assertDoesNotThrow(() -> bcRepo.insert(any()));
        assertEquals(1,id);
    }

    @Test
    void failNewSection() throws IOException {
        when(bcRepo.insert(any())).thenReturn("fileName");
        when(genericRepository.insert(any())).thenReturn(null);
        assertThrows(NullPointerException.class,() -> service.newSection("name", "description", null, null));
    }


    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulShowSection(int id){
        Section section = spy(Section.class);
        section.setId(id);
        section.setBanner("banner");
        section.setDescription("description");
        section.setPicture("picture");
        section.setName("name");
        doReturn(2).when(section).getFollowCount();
        when(genericRepository.findById(Section.class,id)).thenReturn(section);
        SectionPage sectionPage = service.showSection(id);
        assertNotNull(sectionPage);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulShowSectionWithLoggedUser(int id){
        Section section = spy(Section.class);
        section.setId(id);
        section.setBanner("banner");
        section.setDescription("description");
        section.setPicture("picture");
        section.setName("name");
        doReturn(2).when(section).getFollowCount();
        User user = spy(User.class);
        user.setId(1);
        when(currentUser.getId()).thenReturn(1);
        when(currentUser.isLoggedIn()).thenReturn(true);
        when(genericRepository.findById(User.class,1)).thenReturn(user);
        Follow follow = new Follow(user,section);
        doReturn(follow).when(section).getFollow(any());
        when(genericRepository.findById(Section.class,id)).thenReturn(section);
        SectionPage sectionPage = service.showSection(id);
        assertNotNull(sectionPage);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 30})
    void successfulShowSectionWithLoggedUserWithoutFollow(int id){
        Section section = spy(Section.class);
        section.setId(id);
        section.setBanner("banner");
        section.setDescription("description");
        section.setPicture("picture");
        section.setName("name");
        doReturn(2).when(section).getFollowCount();
        User user = spy(User.class);
        user.setId(1);
        when(currentUser.getId()).thenReturn(1);
        when(currentUser.isLoggedIn()).thenReturn(true);
        when(genericRepository.findById(User.class,1)).thenReturn(user);
        doReturn(null).when(section).getFollow(any());
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
        Section section = spy(Section.class);
        section.setId(1);
        section.setBanner("banner");
        section.setDescription("description");
        section.setPicture("picture");
        section.setName(sectionName);
        doReturn(2).when(section).getFollowCount();
        when(genericRepository.findByNaturalId(Section.class,sectionName)).thenReturn(section);
        SectionPage sectionPage = service.getSection(sectionName);
        assertNotNull(sectionPage);
    }

    @ParameterizedTest
    @ValueSource(strings = {"notSection1", "notSection2", "notSection3"})
    void failGetSectionWithWrongName(String wrongName){
        when(genericRepository.findByNaturalId(Section.class,wrongName)).thenReturn(null);
        assertThrows(ConstraintViolationException.class,() -> service.getSection(wrongName));
    }


    @Test
    void getTrendingSections(){
        List<SectionPage> list = service.getTrendingSections();
        assertTrue(list != null);
    }

    @Test
    void getTopSections(){
        List<SectionPage> list = service.getTopSections();
        assertTrue(list != null);
    }

}
