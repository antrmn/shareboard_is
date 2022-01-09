package persistence.repo;

import persistence.model.Post;
import persistence.model.Section;
import persistence.model.User;


import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostRepository extends GenericRepository<Post, Integer> {
    protected PostRepository() {
        super(Post.class);
    }

    public PostFinder getFinder(){
        return new PostFinder();
    }

    private enum SortCriteria {OLDEST, NEWEST, MOSTVOTED};
    public class PostFinder {
        private User author;
        private List<Section> sections;
        private Instant before;
        private Instant after;
        private int offset = 0;
        private int pageSize = 30;
        private String content;
        private boolean includeBody = false;
        private SortCriteria sortCriteria = PostRepository.SortCriteria.NEWEST;

        protected PostFinder(){ }

        public PostFinder byAuthor(User author){
            this.author = author;
            return this;
        }

        public PostFinder byContent(String text){
            this.content = text;
            return this;
        }

        public PostFinder bySections(List<Section> sections){
            this.sections = sections;
            return this;
        }

        public PostFinder bySection(Section section){
            this.sections = Collections.singletonList(section);
            return this;
        }

        public PostFinder postedAfter(Instant after){
            this.after = after;
            return this;
        }

        public PostFinder postedBefore(Instant before){
            this.before = before;
            return this;
        }

        public PostFinder offset(int n){
            offset = n;
            return this;
        }

        public PostFinder limit(int n){
            pageSize = n;
            return this;
        }

        public PostFinder getOldest(){
            sortCriteria = SortCriteria.OLDEST;
            return this;
        }

        public PostFinder getNewest(){
            sortCriteria = SortCriteria.NEWEST;
            return this;
        }

        public PostFinder getMostVoted(){
            sortCriteria = SortCriteria.MOSTVOTED;
            return this;
        }

        public PostFinder includeBody(){
            includeBody = true;
            return this;
        }

        public List<Post> getResults(){
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Post> cq = cb.createQuery(Post.class);
            Root<Post> root = cq.from(Post.class);

            List<Predicate> predicates = new ArrayList<>();

            if(author != null) {
                predicates.add(cb.equal(root.get("author"), author));
            }

            if(sections != null && !sections.isEmpty()) {
                predicates.add(root.get("section").in(sections));
            }

            if(after != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("creationDate"), after));
            }

            if(before != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("creationDate"), before));
            }

            if(content != null) {
                if(includeBody) {
                    predicates.add(
                            cb.or(
                                    cb.like(root.get("title"), '%' + content + '%'),
                                    cb.and(
                                            cb.notEqual(root.get("type"), "IMG"),
                                            cb.like(root.get("content"), '%' + content + '%')
                                    )));
                } else {
                    predicates.add(cb.like(root.get("title"), '%' + content + '%'));
                }
            }

            cq.where(cb.and(predicates.toArray(Predicate[]::new)));

            switch (sortCriteria) {
                case MOSTVOTED:
                    cq.orderBy(cb.desc(root.get("votes")));
                    break;
                case OLDEST:
                    cq.orderBy(cb.asc(root.get("creationDate")));
                    break;
                case NEWEST:
                default:
                    cq.orderBy(cb.desc(root.get("creationDate")));
                    break;
            }


            TypedQuery<Post> tq = em.createQuery(cq);
            if(offset >= 0)
                tq.setFirstResult(offset);
            if(pageSize>=0)
                tq.setMaxResults(pageSize);

            tq.setHint("org.hibernate.readOnly", true); //ottimizzazione per sola lettura in hibernate

            return tq.getResultList();
        }
    }
}
