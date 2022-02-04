package service;

import persistence.model.Section;
import persistence.repo.BinaryContentRepository;
import persistence.repo.GenericRepository;
import persistence.repo.SectionRepository;
import service.auth.AdminsOnly;
import service.dto.SectionPage;
import service.validation.SectionExists;
import service.validation.SectionExistsById;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.BufferedInputStream;
import java.io.IOException;

@ApplicationScoped
@Transactional
public class SectionService {

    @Inject private SectionRepository sectionRepo;
    @Inject private GenericRepository genericRepository;
    @Inject private BinaryContentRepository bcRepo;

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


    public SectionPage showSection(@SectionExistsById int id){
       Section s =  genericRepository.findById(Section.class, id);
       int nFollowers = s.getFollowCount();
       SectionPage sectionData = new SectionPage(id,s.getName(), s.getDescription(), s.getPicture(), s.getBanner(), nFollowers);
       return sectionData;
    }

    public SectionPage getSection(@SectionExists String sectionName){
        Section s = sectionRepo.getByName(sectionName);
        int nFollowers = s.getFollowCount();
        SectionPage section = new SectionPage(s.getId(), s.getName(), s.getDescription(), s.getPicture(), s.getBanner(), nFollowers);
        return section;
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
