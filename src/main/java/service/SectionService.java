package service;

import persistence.model.Section;
import persistence.repo.BinaryContentRepository;
import persistence.repo.FollowRepository;
import persistence.repo.SectionRepository;
import service.dto.SectionPage;
import service.validation.SectionExists;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.BufferedInputStream;
import java.io.IOException;

public class SectionService {

    @Inject private SectionRepository sectionRepo;
    @Inject private FollowRepository followRepo;
    @Inject private BinaryContentRepository bcRepo;

    @RolesAllowed({"admin"})
    @Transactional
    public void Delete(@SectionExists int id){
        sectionRepo.remove(sectionRepo.findById(id));
    }

    @RolesAllowed({"admin"})
    @Transactional
    public int NewSection(SectionPage sectiondata,
                          BufferedInputStream picture,
                          BufferedInputStream banner) throws IOException {
        Section s = new Section();
        s.setName(sectiondata.getName());
        s.setPicture(sectiondata.getPicture());
        s.setBanner(sectiondata.getBanner());
        s.setDescription(sectiondata.getDescription());
        bcRepo.insert(picture);
        bcRepo.insert(banner);

        return sectionRepo.insert(s).getId();
    }

    public SectionPage ShowSection(@SectionExists int id){
       Section s =  sectionRepo.findById(id);
       int nFollowers = followRepo.getBySection(s).size();
       SectionPage sectionData = new SectionPage(id,s.getName(), s.getDescription(), s.getPicture(), s.getBanner(), nFollowers);
       return sectionData;
    }

    public Section getSection(@SectionExists int id){
        return sectionRepo.findById(id);
    }

    public Section getSection(String sectionName){ return sectionRepo.getByName(sectionName); } //todo: notazione sectionExists per il nome

    @RolesAllowed({"admin"})
    @Transactional
    public void EditSection(SectionPage edit,
                            @SectionExists int id,
                            BufferedInputStream picture,
                            BufferedInputStream banner) throws IOException {
        Section s = sectionRepo.findById(id);
        s.setBanner(edit.getBanner());
        s.setPicture(edit.getPicture());
        s.setDescription(edit.getDescription());
        bcRepo.insert(picture);
        bcRepo.insert(banner);
    }
}
