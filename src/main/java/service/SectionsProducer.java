package service;

import service.dto.SectionPage;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@ApplicationScoped
public class SectionsProducer {
    @Inject SectionService service;

    @Named("sections")
    @RequestScoped
    @Produces
    public List<SectionPage> produceSections(){
        return service.showSections();
    }

}
