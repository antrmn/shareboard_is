package service;

import persistence.model.Section;
import persistence.model.User;
import persistence.repo.BinaryContentRepository;
import persistence.repo.GenericRepository;
import persistence.repo.SectionRepository;
import service.auth.AdminsOnly;
import service.dto.CurrentUser;
import service.dto.SectionPage;
import service.validation.Image;
import service.validation.SectionExists;
import service.validation.UniqueSection;
import util.ReadLimitExceededException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class SectionService {
    private SectionRepository sectionRepo;
    private GenericRepository genericRepository;
    private BinaryContentRepository bcRepo;
    private CurrentUser currentUser;

    protected SectionService(){}

    @Inject
    protected SectionService(GenericRepository genericRepository, SectionRepository sectionRepository,
                             BinaryContentRepository bcRepo, CurrentUser currentUser){
        this.genericRepository = genericRepository;
        this.sectionRepo = sectionRepository;
        this.bcRepo = bcRepo;
        this.currentUser = currentUser;
    }

    /**
     * Converte section in SectionPage.
     * @param section sezione da convertire
     * @return SectionPage con i dati di section
     */
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

    /**
     * Cancella una sezione dato il suo id
     * @param id l'id di una sezione esistente
     */
    @AdminsOnly
    public void delete(@SectionExists int id){
        genericRepository.remove(genericRepository.findById(Section.class, id));
    }

    /**
     * Crea una nuova sezione e ne restituisce l'id
     * @param name Nome della sezione
     * @param description descrizione della sezione
     * @param picture stream relativo alla foto della sezione
     * @param banner stream relativo al banner della sezione
     * @return id della sezione creata
     */
    @AdminsOnly
    public int newSection(@NotBlank @Size(max=50) @UniqueSection String name,
                          @Size(max=255) String description,
                          @Image BufferedInputStream picture,
                          @Image BufferedInputStream banner) {
        Section s = new Section();
        s.setName(name);
        s.setDescription(description);
        try {
            if (picture != null)
                s.setPicture(bcRepo.insert(picture));
            if (banner != null)
                s.setBanner(bcRepo.insert(banner));
        } catch (ReadLimitExceededException e) {
            throw new IllegalArgumentException("Il file non deve superare i 5MB");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return genericRepository.insert(s).getId();
    }

    /**
     * Ritorna una lista di tutte le sezioni esistenti
     * @return lista di sezioni
     */
    public List<SectionPage> showSections(){
        return genericRepository.findAll(Section.class).stream().map(this::map).collect(Collectors.toList());
    }

    /**
     * Ritorna una mappa di tutte le sezioni esistenti
     * @return mappa la cui chiave è l'id della sezione e il valore un entita PostPage che ne contiene i dati
     */
    @Named("sections")
    @RequestScoped
    @Produces
    public Map<Integer,SectionPage> getSectionsMap(){
        return genericRepository.findAll(Section.class).stream().map(this::map)
                .collect(Collectors.toMap(SectionPage::getId, section -> section));
    }

    /**
     * Ritorna un entità sezione dato un certo id
     * @param id id di una sezione esistente
     * @return entita SectionPage che contiene i dati della sezione
     */
    public SectionPage showSection(@SectionExists int id){
       Section s =  genericRepository.findById(Section.class, id);
       return map(s);
    }

    /**
     * Ritorna un entità sezione con un nome specifico
     * @param sectionName nome di una sezione esistente
     * @return entita SectionPage che contiene i dati della sezione
     */
    public SectionPage getSection(@NotNull @SectionExists String sectionName){
        Section s = genericRepository.findByNaturalId(Section.class,sectionName);
        return map(s);
    }

    /**
     * Ritorna una lista delle sezioni con più follows
     * @return lista di sezioni
     */
    @Produces
    @RequestScoped
    @Named("topSections")
    public List<SectionPage> getTopSections(){
        return sectionRepo.getMostFollowedSections().stream().map(this::map).collect(Collectors.toList());
    }

    /**
     * Ritorna una lista delle sezioni con più follows negli ultimi 7 giorni
     * @return lista di sezioni
     */
    @Produces
    @RequestScoped
    @Named("trendingSections")
    public List<SectionPage> getTrendingSections(){
        return sectionRepo.getMostFollowedSections(Instant.now().minus(7, ChronoUnit.DAYS))
                .stream().map(this::map).collect(Collectors.toList());
    }
}
