package usecase.post;

import domain.entity.Post;
import domain.entity.Section;
import domain.entity.User;
import domain.repository.GenericRepository;
import domain.repository.PostRepository;
import media.MediaRepository;
import media.ReadLimitExceededException;
import media.validator.Image;
import usecase.auth.AuthenticationRequired;
import usecase.auth.AuthorizationException;
import usecase.auth.CurrentUser;
import usecase.auth.DenyBannedUsers;
import usecase.post.validator.PostExists;
import usecase.section.validator.SectionExists;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static domain.entity.Post.Type.IMG;
import static domain.entity.Post.Type.TEXT;
import static usecase.post.PostSearchForm.SortCriteria.NEWEST;


@ApplicationScoped
@Transactional
public class PostService {
    private GenericRepository genericRepository;
    private PostRepository postRepo;
    private MediaRepository bcRepo;
    private CurrentUser currentUser;

    protected PostService(){}

    @Inject
    protected PostService(GenericRepository genericRepository, PostRepository postRepo,
                          MediaRepository bcRepo, CurrentUser currentUser){
        this.genericRepository = genericRepository;
        this.postRepo = postRepo;
        this.bcRepo = bcRepo;
        this.currentUser = currentUser;
    }

    /**
     * Converte post in PostPage.
     * @param post post da convertire
     * @return PostPage con i dati di post
     */
    private PostPage mapPost(Post post){
        User user = null;
        if(currentUser.isLoggedIn())
            user = genericRepository.findById(User.class,currentUser.getId());

        //converte persistence.Post.Type in usecase.post.PostType (ew)
        PostType postType = post.getType() == IMG ? PostType.IMG : PostType.TEXT;

        return PostPage.builder()
                .id(post.getId())
                .title(post.getTitle())
                .vote(post.getVote(user) == null ? 0 : post.getVote(user).getVote())
                .votes(post.getVotesCount() == null ? 0 : post.getVotesCount())
                .sectionName(post.getSection().getName())
                .authorName(post.getAuthor().getUsername())
                .sectionId(post.getSection().getId())
                .authorId(post.getAuthor().getId())
                .content(post.getContent())
                .creationDate(post.getCreationDate() == null ? Instant.now() : post.getCreationDate())
                .nComments(post.getCommentCount())
                .type(postType)
                .build();
    }

    /**
     * Ritorna un entità DTO relativa ad un post
     * @param id identificativo post
     * @return DTO di un post
     */
    public PostPage getPost(@PostExists int id){
        Post p = genericRepository.findById(Post.class,id);
        return mapPost(p);
    }

    /**
     * Ritorna una lista di post che rispettano determinati parametri
     * @param form entità con i parametri di ricerca
     * @return lista di PostPage
     */
    @Transactional
    public List<PostPage> loadPosts(PostSearchForm form){
        final int elementsPerPage = 10;
        PostRepository.PostFinder finder = postRepo.getFinder();

        finder.limit(elementsPerPage);

        int page = form.getPage() > 0 ? form.getPage() : 1;
        finder.offset((page == 1) ? 0 : (page-1) * elementsPerPage+1);

        if(form.getContent() != null) {
            finder.byContent(form.getContent());
        }
        if(form.isIncludeBody()){
            finder.includeBody();
        }
        if(form.getSectionName() != null){
            Section section = genericRepository.findByNaturalId(Section.class, form.getSectionName());
            if(section != null){
                finder.bySection(section);
            }
        }
        if(form.getAuthorName() != null){
            User user = genericRepository.findByNaturalId(User.class, form.getAuthorName());
            if(user != null){
                finder.byAuthor(user);
            }
        }
        if(form.isOnlyFollow() && currentUser.isLoggedIn()){
            finder.joinUserFollows(genericRepository.findById(User.class,currentUser.getId()));
        }
        if(form.getPostedAfter() != null){
            finder.postedAfter(form.getPostedAfter());
        }
        if(form.getPostedBefore() != null){
            finder.postedBefore(form.getPostedBefore());
        }

        //null-safe switch
        switch(form.getOrderBy() != null ? form.getOrderBy() : NEWEST){
            case NEWEST: default: {
                finder.getNewest();
                break;
            }
            case OLDEST:{
                finder.getOldest();
                break;
            }
            case MOSTVOTED:{
                finder.getMostVoted();
                break;
            }
        }
        return finder.getResults().stream().map(this::mapPost).collect(Collectors.toList());
    }

    /**
     * Elimina un post dato il suo id
     * @param id identificativo del post
     */
    @AuthenticationRequired //TODO: check autore post?
    @DenyBannedUsers
    public void delete(@PostExists int id){
        Post post = genericRepository.findById(Post.class,id);
        if(currentUser.getId() != post.getAuthor().getId() && !currentUser.isAdmin())
            throw new AuthorizationException();
        genericRepository.remove(genericRepository.findById(User.class,id));
    }

    /**
     * Aggiunge un post ad una sezione
     * @param title titolo del post
     * @param body testo del post
     * @param sectionName nome della sezione
     * @return identificativo del nuovo post creato
     */
    @AuthenticationRequired
    @DenyBannedUsers
    public int newPost(@NotBlank String title,
                       @Size(max=65535) String body,
                       @NotNull @SectionExists String sectionName){
        User user = genericRepository.findByNaturalId(User.class, currentUser.getUsername());
        Section section = genericRepository.findByNaturalId(Section.class,sectionName);


        Post post = new Post();
        post.setTitle(title);
        post.setContent(body == null || body.isBlank() ? "" : body);
        post.setAuthor(user);
        post.setSection(section);
        post.setType(TEXT);

        return genericRepository.insert(post).getId();
    }

    /**
     * Aggiunge un post ad una sezione
     * @param title titolo del post
     * @param content file di tipo immagine
     * @param sectionName nome della sezione
     * @return identificativo del nuovo post creato
     */
    @AuthenticationRequired
    @DenyBannedUsers
    public int newPost(@NotBlank @Size(max=255) String title,
                       @NotNull @Image BufferedInputStream content,
                       @NotNull @SectionExists String sectionName){
        User user = genericRepository.findByNaturalId(User.class,currentUser.getUsername());
        Section section = genericRepository.findByNaturalId(Section.class,sectionName);

        String fileName;
        try {
            fileName = bcRepo.insert(content);
        } catch (ReadLimitExceededException e){
            throw new IllegalArgumentException("Il file non deve superare i 5MB");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Post post = new Post();
        post.setTitle(title);
        post.setAuthor(user);
        post.setContent(fileName);
        post.setSection(section);
        post.setType(IMG);

        return genericRepository.insert(post).getId(); //in caso di errore il file resta
    }
}
