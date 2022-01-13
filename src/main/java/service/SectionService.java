package service;

import persistence.model.Section;
import persistence.repo.SectionRepository;
import service.dto.SectionPage;
import service.validation.SectionExists;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;

public class SectionService {

    @Inject
    private SectionRepository sectionRepo;

    @RolesAllowed({"admin"})
    @Transactional
    public void Delete(@SectionExists int id){
        sectionRepo.remove(sectionRepo.findById(id));
    }

    @RolesAllowed({"admin"})
    @Transactional
    public int NewSection(SectionPage sectiondata){
        Section s = new Section();
        s.setName(sectiondata.getName());
        s.setPicture(sectiondata.getPicture());
        s.setBanner(sectiondata.getBanner());
        s.setDescription(sectiondata.getDescription());
        return sectionRepo.insert(s).getId();
    }

    public SectionPage ShowSection(@SectionExists int id){
       Section s =  sectionRepo.findById(id);
       SectionPage sectionData = new SectionPage(s.getId(), s.getName(), s.getDescription(), s.getPicture(), s.getBanner(), 0); //come ottengo i followers?
       return sectionData;
    }

    @RolesAllowed({"admin"})
    @Transactional
    public void EditSection(SectionPage edit, @SectionExists int id){
        Section s = sectionRepo.findById(id);
        s.setBanner(edit.getBanner());
        s.setPicture(edit.getPicture());
        s.setDescription(edit.getDescription());
    }
}
