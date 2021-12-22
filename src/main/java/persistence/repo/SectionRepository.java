package persistence.repo;

import persistence.model.Section;
import org.hibernate.Session;

public class SectionRepository extends GenericRepository<Section, Integer>{

    public SectionRepository() {
        super(Section.class);
    }

    public Section getByName(String name){
        return em.unwrap(Session.class).bySimpleNaturalId(Section.class).load(name);
    }

    public Section getReferenceByName(String name){
        return em.unwrap(Session.class).bySimpleNaturalId(Section.class).getReference(Section.class);
    }
}
