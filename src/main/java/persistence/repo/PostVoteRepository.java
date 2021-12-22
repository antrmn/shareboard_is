package persistence.repo;

import persistence.model.Post;
import persistence.model.PostVote;
import persistence.model.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PostVoteRepository extends GenericRepository<PostVote, PostVote.Id>{
    public PostVoteRepository() {
        super(PostVote.class);
    }

    public Map<Post, PostVote> findByUser(User user, List<Post> posts){
        return em.createQuery("from PostVote pv where pv.id.user in :user and pv.id.post in :posts", PostVote.class)
                .setParameter("user",user)
                .setParameter("posts", posts)
                .setHint("org.hibernate.readOnly", true)
                .getResultStream().collect(Collectors.toMap(
                        res -> res.getId().getPost(),
                        res -> res
                ));
    }
}
