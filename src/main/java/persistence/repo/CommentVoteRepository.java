package persistence.repo;

import persistence.model.Comment;
import persistence.model.CommentVote;
import persistence.model.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommentVoteRepository extends AbstractRepository<CommentVote, CommentVote.Id> {
    public CommentVoteRepository() {
        super(CommentVote.class);
    }

    public Map<Comment, CommentVote> findByUser(User user, List<Comment> comments){
        return em.createQuery("from CommentVote cv where cv.id.user in :user and cv.id.comment in :comments",
                CommentVote.class)
                .setParameter("user",user)
                .setParameter("comments", comments)
                .setHint("org.hibernate.readOnly", true)
                .getResultStream().collect(Collectors.toMap(
                        res -> res.getId().getComment(),
                        res -> res
                ));
    }
}
