package usecase.follow;

import domain.entity.Follow;
import domain.entity.Section;
import domain.entity.User;
import domain.repository.GenericRepository;
import domain.validation.SectionExists;
import usecase.auth.AuthenticationRequired;
import usecase.auth.CurrentUser;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class FollowService {
    private GenericRepository genericRepository;
    private CurrentUser currentUser;

    protected FollowService(){}

    @Inject
    protected FollowService(GenericRepository genericRepository, CurrentUser currentUser){
        this.genericRepository = genericRepository;
        this.currentUser = currentUser;
    }

    /**
     * Permette di seguire una sezione
     * @param sectionId identificativo sezione
     */
    @AuthenticationRequired
    public void follow(@SectionExists int sectionId){
        User user = genericRepository.findById(User.class, currentUser.getId());
        Section section = genericRepository.findById(Section.class,sectionId);
        genericRepository.insert(new Follow(user,section));
    }

    /**
     * Permette di togliere il follow ad una sezione
     * @param sectionId identificativo sezione
     */
    @AuthenticationRequired
    public void unFollow(@SectionExists int sectionId){
        User user = genericRepository.findById(User.class, currentUser.getId());
        Section section = genericRepository.findById(Section.class,sectionId);
        Follow follow = section.getFollow(user);

        if(follow != null)
            genericRepository.remove(follow);
    }

}
