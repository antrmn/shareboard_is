package domain.repository;

import domain.entity.Post;
import domain.entity.Section;
import domain.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe che incapsula la logica per il recupero di entità di tipo {@link Post}
 */
public class PostRepository implements Serializable {

    @PersistenceContext
    protected EntityManager em;

    /**
     * Restituisce una nuova istanza di PostFinder
     * @return nuova istanza di PostFinder
     */
    public PostFinder getFinder(){
        return new PostFinder();
    }

    private enum SortCriteria {OLDEST, NEWEST, MOSTVOTED};

    /**
     *  Classe interna usata per specificare i parametri di ricerca di un post
     */
    public class PostFinder {
        private User author;
        private List<Section> sections;
        private Instant before;
        private Instant after;
        private int offset = 0;
        private int pageSize = 30;
        private String content;
        private boolean includeBody = false;
        private User joinUserFollows;
        private SortCriteria sortCriteria = PostRepository.SortCriteria.NEWEST;

        protected PostFinder(){ }

        /**
         * Setta il campo author e restituisce l'istanza passata di PostFinder
         * @param author entità User dell'autore dei post
         * @return istanza passata di PostFinder
         */
        public PostFinder byAuthor(User author){
            this.author = author;
            return this;
        }

        /**
         * Setta il campo content e restituisce l'istanza passata di PostFinder
         * @param author text contenuto dei post
         * @return istanza passata di PostFinder
         */
        public PostFinder byContent(String text){
            this.content = text;
            return this;
        }

        /**
         * Setta il campo sections e restituisce l'istanza passata di PostFinder
         * @param sections lista di sezioni
         * @return istanza passata di PostFinder
         */
        public PostFinder bySections(List<Section> sections){
            this.sections = sections;
            return this;
        }

        /**
         * Setta il campo sections e restituisce l'istanza passata di PostFinder
         * @param sections sezione dei post
         * @return istanza passata di PostFinder
         */
        public PostFinder bySection(Section section){
            this.sections = Collections.singletonList(section);
            return this;
        }

        /**
         * Setta il campo postedAfter e restituisce l'istanza passata di PostFinder
         * @param after data dopo la quale i post da trovare sono stati postati
         * @return istanza passata di PostFinder
         */
        public PostFinder postedAfter(Instant after){
            this.after = after;
            return this;
        }

        /**
         * Setta il campo postedBefore e restituisce l'istanza passata di PostFinder
         * @param before data prima della quale i post da trovare sono stati postati
         * @return istanza passata di PostFinder
         */
        public PostFinder postedBefore(Instant before){
            this.before = before;
            return this;
        }

        /**
         * Setta il campo offset e restituisce l'istanza passata di PostFinder
         * @param n offset per la paginazione
         * @return istanza passata di PostFinder
         */
        public PostFinder offset(int n){
            offset = n;
            return this;
        }

        /**
         * Setta il campo limit e restituisce l'istanza passata di PostFinder
         * @param n limite di post da caricare
         * @return istanza passata di PostFinder
         */
        public PostFinder limit(int n){
            pageSize = n;
            return this;
        }

        /**
         * Setta il campo sortCriteria a oldest e restituisce l'istanza passata di PostFinder
         * @return istanza passata di PostFinder
         */
        public PostFinder getOldest(){
            sortCriteria = SortCriteria.OLDEST;
            return this;
        }

        /**
         * Setta il campo sortCriteria a newest e restituisce l'istanza passata di PostFinder
         * @return istanza passata di PostFinder
         */
        public PostFinder getNewest(){
            sortCriteria = SortCriteria.NEWEST;
            return this;
        }

        /**
         * Setta il campo sortCriteria a most voted e restituisce l'istanza passata di PostFinder
         * @return istanza passata di PostFinder
         */
        public PostFinder getMostVoted(){
            sortCriteria = SortCriteria.MOSTVOTED;
            return this;
        }

        /**
         * Setta il campo includeBody a true e restituisce l'istanza passata di PostFinder
         * @return istanza passata di PostFinder
         */
        public PostFinder includeBody(){
            includeBody = true;
            return this;
        }

        /**
         * Setta il campo joinUserFollows e restituisce l'istanza passata di PostFinder
         * @param user entita utente da cui ottenere le sezioni seguite
         * @return istanza passata di PostFinder
         */
        public PostFinder joinUserFollows(User user){
            joinUserFollows = user;
            return this;
        }

        /**
         * Restituisce tutti i post che rispettano i criteri di ricerca
         * @return lista di entità Post
         */
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
                                            cb.notEqual(root.get("type"), Post.Type.IMG),
                                            cb.like(root.get("content"), '%' + content + '%')
                                    )));
                } else {
                    predicates.add(cb.like(root.get("title"), '%' + content + '%'));
                }
            }

            if(joinUserFollows != null){
                Join<Object, Object> joinFollow = root.join("section").join("follows");
                predicates.add(cb.equal(joinFollow.get("user"), joinUserFollows));
            }

            cq.where(cb.and(predicates.toArray(Predicate[]::new)));

            switch (sortCriteria) {
                case MOSTVOTED:
                    cq.orderBy(cb.desc(root.get("votesCount")));
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

            return tq.getResultList();
        }
    }
}
