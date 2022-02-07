package service;

import persistence.model.Section;
import persistence.model.User;
import persistence.repo.BinaryContentRepository;
import persistence.repo.GenericRepository;
import persistence.repo.SectionRepository;
import service.auth.AdminsOnly;
import service.dto.CurrentUser;
import service.dto.SectionPage;
import service.validation.SectionExists;
import service.validation.SectionExistsById;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class SectionService {

    @Inject private SectionRepository sectionRepo;
    @Inject private GenericRepository genericRepository;
    @Inject private BinaryContentRepository bcRepo;
    @Inject private CurrentUser currentUser;

    private SectionPage map(Section section){
        User user = null;
        if(currentUser.isLoggedIn()){
            user = genericRepository.findById(User.class,currentUser.getId());
        }
        return SectionPage.builder().id(section.getId())
                .name(section.getName())
                .picture(section.getPicture())
                .description(section.getDescription())
                .banner(section.getBanner())
                .nFollowers(section.getFollowCount())
                .isFollowed(user != null && section.getFollow(user) != null).build();
    }

    @AdminsOnly
    public void delete(@SectionExistsById int id){
        genericRepository.remove(genericRepository.findById(Section.class, id));
    }

    @AdminsOnly
    public int newSection(SectionPage sectiondata,
                          BufferedInputStream picture,
                          BufferedInputStream banner) throws IOException {
        Section s = new Section();
        s.setName(sectiondata.getName());
        s.setPicture(sectiondata.getPicture());
        s.setBanner(sectiondata.getBanner());
        s.setDescription(sectiondata.getDescription());
        bcRepo.insert(picture);
        bcRepo.insert(banner);

        return genericRepository.insert(s).getId();
    }

    public List<SectionPage> showSections(){
        return genericRepository.findAll(Section.class).stream().map(this::map).collect(Collectors.toList());
    }

    @Named("sections")
    @RequestScoped
    @Produces
    public Map<Integer,SectionPage> getSectionsMap(){
        return genericRepository.findAll(Section.class).stream().map(this::map)
                .collect(Collectors.toMap(SectionPage::getId, section -> section));
    }

    public SectionPage showSection(@SectionExistsById int id){
       Section s =  genericRepository.findById(Section.class, id);
       return map(s);
    }

    public SectionPage getSection(@SectionExists String sectionName){
        Section s = sectionRepo.getByName(sectionName);
        return map(s);
    }

    @AdminsOnly
    public void editSection(SectionPage edit,
                            @SectionExistsById int id,
                            BufferedInputStream picture,
                            BufferedInputStream banner) throws IOException {
        Section s = genericRepository.findById(Section.class,id);
        s.setBanner(edit.getBanner());
        s.setPicture(edit.getPicture());
        s.setDescription(edit.getDescription());
        bcRepo.insert(picture);
        bcRepo.insert(banner);
    }
}
