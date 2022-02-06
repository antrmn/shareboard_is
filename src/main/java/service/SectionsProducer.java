package service;

import service.dto.SectionPage;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class SectionsProducer {
    @Inject SectionService service;

    @Named("sections")
    @RequestScoped
    @Produces
    public Map<Integer,SectionPage> produceSections(){
        return service.showSections().stream().collect(Collectors.toMap(SectionPage::getId, section -> section));
    }

}
