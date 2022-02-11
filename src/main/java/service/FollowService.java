package service;

import persistence.model.Follow;
import persistence.model.Section;
import persistence.model.User;
import persistence.repo.GenericRepository;
import service.auth.AuthenticationRequired;
import service.dto.CurrentUser;
import service.validation.SectionExistsById;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class FollowService {
    @Inject private GenericRepository genericRepository;
    @Inject private CurrentUser currentUser;

    /**
     * Permette di seguire una sezione
     * @param sectionId identificativo sezione
     * @return entità follow inserita nel database
     */
    @AuthenticationRequired
    public Follow follow(@SectionExistsById int sectionId){
        User user = genericRepository.findById(User.class, currentUser.getId());
        Section section = genericRepository.findById(Section.class,sectionId);
        return genericRepository.insert(new Follow(user,section));
    }

    /**
     * Permette di togliere il follow ad una sezione
     * @param sectionId identificativo sezione
     */
    @AuthenticationRequired
    public void unFollow(@SectionExistsById int sectionId){
        User user = genericRepository.findById(User.class, currentUser.getId());
        Section section = genericRepository.findById(Section.class,sectionId);
        Follow follow = section.getFollow(user);

        if(follow != null)
            genericRepository.remove(follow);
    }

}
