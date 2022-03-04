package domain.repository;

import domain.entity.*;
import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.inject.Inject;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Classes(cdi = true, value = {GenericRepository.class, PostRepository.class})
public class PostRepositoryIT extends PersistenceIT{

    @Inject private GenericRepository genericRepository;
    @Inject private PostRepository postRepository;

    private List<User> users;
    private List<Post> posts;
    private List<Section> sections;
    private Map<Integer,List<Comment>> comments = new HashMap<>();

    @BeforeAll
    public void populate() throws Exception {
        doTransactional(() -> {
            users = IntStream.range(1, 4).mapToObj(n -> {
                User user = new User();
                user.setUsername("usecase/user" + n);
                user.setEmail("usecase/user" + n + "@email.it");
                user.setDescription("Description for user " + n);
                user.setAdmin(n % 2 == 0);
                user.setPicture("picture" + n);
                user.setPassword((n + "password").getBytes(StandardCharsets.UTF_8));
                user.setSalt((n + "salt").getBytes(StandardCharsets.UTF_8));
                return user;
            }).map(genericRepository::insert).collect(Collectors.toList());

            sections = IntStream.range(1,4).mapToObj(n -> {
                Section section = new Section();
                section.setName("usecase/section" +n);
                section.setPicture("picture" + n);
                section.setBanner("banner"+n);
                return section;
            }).map(genericRepository::insert).collect(Collectors.toList());

            posts = IntStream.range(1,10).mapToObj(n -> {
                Post post = new Post();
                post.setContent("content"+n);
                post.setTitle("title"+n);
                post.setType(n % 2 == 0 ? Post.Type.TEXT : Post.Type.IMG);
                post.setAuthor( users.get( n % users.size() ) );
                post.setSection( sections.get(n % sections.size()) );
                return post;
            }).map(genericRepository::insert).collect(Collectors.toList());

            IntStream.range(1,30).forEach(n -> {
                Comment comment = new Comment();
                comment.setContent("content" + n);
                comment.setPost( posts.get(n % posts.size() ));
                List<Comment> postComments = comments.computeIfAbsent(comment.getPost().getId(), ArrayList::new);

                if(postComments.size()<=3){
                    comment.setParentComment(null);
                } else {
                    comment.setParentComment(postComments.get(n % postComments.size()));
                }

                postComments.add(comment);
                genericRepository.insert(comment);
            });
        });
    }

    private boolean equalsUnorderedList(List<?> list1, List<?> list2){
        if(list1.size() != list2.size())
            return false;
        return new HashSet<>(list1).equals(new HashSet<>(list2));
    }

    private boolean equalsUnorderedStream(Stream<?> stream1, Stream<?> stream2){
        return equalsUnorderedList(stream1.collect(Collectors.toList()), stream2.collect(Collectors.toList()));
    }

    @Test
    public void findByAuthor() throws Exception {
        User author = users.get(0);
        List<Post> results = postRepository.getFinder().byAuthor(users.get(0)).getResults();
        Assertions.assertTrue(results.stream().allMatch(x -> x.getAuthor().getId().equals(author.getId())));
        Assertions.assertEquals(
                results.size(),
                posts.stream().filter(x -> x.getAuthor().getId().equals(author.getId()))
                        .count());
    }


    @Test
    public void findByContent() throws Exception{
         List<Post> posts = IntStream.range(0, 4).mapToObj(n -> {
            Post post = new Post();
            post.setSection(sections.get(0));
            post.setAuthor(users.get(0));
            return post;
        }).collect(Collectors.toList());

         posts.get(0).setTitle("post con titolo");
         posts.get(0).setType(Post.Type.TEXT);
         posts.get(0).setContent("foo");

         posts.get(1).setTitle("post testuale con titolo e corpo foo");
         posts.get(1).setContent("contenuto");
         posts.get(1).setType(Post.Type.TEXT);

         posts.get(2).setTitle("post immagine");
         posts.get(2).setContent("url foo");
         posts.get(2).setType(Post.Type.IMG);

         posts.get(3).setTitle("post con solo titolo foo");
         posts.get(3).setType(Post.Type.TEXT);
         posts.get(3).setContent("");



        doThenRollback(() -> {
            posts.forEach(genericRepository::insert);
            {
                //cerca per solo titolo
                List<Integer> resultIds = postRepository.getFinder().byContent("foo").getResults().stream().map(Post::getId).collect(Collectors.toList());
                List<Integer> expectedIds = IntStream.of(1, 3).mapToObj(posts::get).map(Post::getId).collect(Collectors.toList());

                Assertions.assertTrue(equalsUnorderedList(resultIds,expectedIds));
            }
            {
                //cerca anche per corpo
                List<Integer> resultIds = postRepository.getFinder().byContent("foo").includeBody().getResults().stream().map(Post::getId).collect(Collectors.toList());
                List<Integer> expectedIds = IntStream.of(0,1, 3).mapToObj(posts::get).map(Post::getId).collect(Collectors.toList());

                Assertions.assertTrue(equalsUnorderedList(resultIds,expectedIds));
            }
        });
    }

    @Test
    public void findBySections() throws Exception{
        doThenRollback(() -> {
            List<Integer> foundPostIds = postRepository.getFinder().bySection(sections.get(0)).getResults()
                    .stream().map(Post::getId).collect(Collectors.toList());
            List<Integer> expectedPostIds = posts.stream()
                    .filter(x -> x.getSection().getId().equals(sections.get(0).getId()))
                    .map(Post::getId).collect(Collectors.toList());

            Assertions.assertTrue(equalsUnorderedList(foundPostIds,expectedPostIds));
        });

        doThenRollback(() -> {
            List<Integer> foundPostIds = postRepository.getFinder().bySections(sections.subList(0,2)).getResults()
                    .stream().map(Post::getId).collect(Collectors.toList());
            List<Integer> expectedPostIds = posts.stream()
                    .filter(x -> x.getSection().getId().equals(sections.get(0).getId())
                            || x.getSection().getId().equals(sections.get(1).getId()))
                    .map(Post::getId).collect(Collectors.toList());

            Assertions.assertTrue(equalsUnorderedList(foundPostIds,expectedPostIds));
        });
    }

    @Test
    void postedAfterBefore() throws Exception {
        Instant creationDate = Instant.now().minus(7, ChronoUnit.DAYS);
        List<Integer> rollbackIds = doThenRollback((em) -> {
            List<Integer> expected = IntStream.range(0,10).mapToObj(n -> {
                em.createNativeQuery(
                                "insert into post(section_id, author_id, title, content, type, creation_date) " +
                                        "values(?1,?2,?3,?4,?5,?6)")
                        .setParameter(1, sections.get(0).getId())
                        .setParameter(2, users.get(0).getId())
                        .setParameter(3, "title" + n)
                        .setParameter(4, "content" + n)
                        .setParameter(5, "IMG")
                        .setParameter(6, Date.from(creationDate)).executeUpdate();
                return ((BigInteger) em.createNativeQuery("SELECT LAST_INSERT_ID()").getSingleResult()).intValue();
            }).collect(Collectors.toList());

            {
                List<Integer> result = postRepository.getFinder().postedBefore(creationDate.plus(1, ChronoUnit.DAYS))
                        .getResults().stream().map(Post::getId).collect(Collectors.toList());
                Assertions.assertTrue(equalsUnorderedList(expected,result));
            }
            {
                List<Integer> result = postRepository.getFinder().postedBefore(creationDate)
                        .getResults().stream().map(Post::getId).collect(Collectors.toList());
                Assertions.assertTrue(equalsUnorderedList(expected,result));
            }
            {
                List<Integer> result = postRepository.getFinder().postedBefore(creationDate).postedAfter(creationDate)
                        .getResults().stream().map(Post::getId).collect(Collectors.toList());
                Assertions.assertTrue(equalsUnorderedList(expected,result));
            }
            {
                List<Integer> result = postRepository.getFinder().postedAfter(creationDate)
                        .getResults().stream().map(Post::getId).collect(Collectors.toList());
                Assertions.assertEquals(result.size(),expected.size() + posts.size());
            }
            return expected;
        });

        rollbackIds.stream().map(id -> genericRepository.findById(Post.class,id)).forEach(Assertions::assertNull);
    }

    @Test
    void sortByTimeTest() throws Exception{
        Instant creationDate = Instant.now().minus(7, ChronoUnit.DAYS);
        doThenRollback((em) -> {
            List<Integer> expected = IntStream.range(0,10).mapToObj(n -> {
                em.createNativeQuery(
                                "insert into post(section_id, author_id, title, content, type, creation_date) " +
                                        "values(?1,?2,?3,?4,?5,?6)")
                        .setParameter(1, sections.get(0).getId())
                        .setParameter(2, users.get(0).getId())
                        .setParameter(3, "title" + n)
                        .setParameter(4, "content" + n)
                        .setParameter(5, "IMG")
                        .setParameter(6, Date.from(creationDate)).executeUpdate();
                return ((BigInteger) em.createNativeQuery("SELECT LAST_INSERT_ID()").getSingleResult()).intValue();
            }).collect(Collectors.toList());

            List<Integer> idOldest = postRepository.getFinder().getOldest().getResults().stream().map(Post::getId)
                    .collect(Collectors.toList());
            Assertions.assertEquals(idOldest.size(),expected.size() + posts.size());
            Assertions.assertTrue(equalsUnorderedList(idOldest.subList(0,expected.size()), expected));

            List<Integer> idNewest = postRepository.getFinder().getNewest().getResults().stream().map(Post::getId)
                    .collect(Collectors.toList());
            List<Integer> postsIds = posts.stream().map(Post::getId).collect(Collectors.toList());
            Assertions.assertEquals(idNewest.size(),expected.size() + posts.size());
            Assertions.assertTrue(equalsUnorderedList(idNewest.subList(0,posts.size()), postsIds));
        });
    }

    @Test
    void joinUserFollowTest() throws Exception {
        doThenRollback(() -> {
            User user = genericRepository.findById(User.class,users.get(0).getId());
            Section section1 = genericRepository.findById(Section.class, sections.get(0).getId());
            Section section2 = genericRepository.findById(Section.class, sections.get(1).getId());
            Follow follow1 = new Follow(user,section1);
            Follow follow2 = new Follow(user,section2);

            genericRepository.insert(follow1);
            genericRepository.insert(follow2);

            Stream<Integer> result = postRepository.getFinder().joinUserFollows(users.get(0)).getResults().stream().map(Post::getId);
            Stream<Integer> expected = posts.stream()
                    .filter(x -> x.getSection().getId().equals(sections.get(0).getId()) ||
                    x.getSection().getId().equals(sections.get(1).getId()))
                    .map(Post::getId);

            Assertions.assertTrue(equalsUnorderedStream(result,expected));
            List<Post> results = postRepository.getFinder().joinUserFollows(users.get(1)).getResults();
            Assertions.assertTrue(results.isEmpty());
        });
    }

    @Test
    void sortByVoteTest() throws Exception{
        doThenRollback((em) -> {
            posts.subList(0,5).forEach(p -> {
                User user = genericRepository.findById(User.class, users.get(0).getId());
                Post post = genericRepository.findById(Post.class,p.getId());
                PostVote postVote = new PostVote(user,post,(short)+1);
                genericRepository.insert(postVote);
            });
            posts.subList(5,7).forEach(p -> {
                User user1 = genericRepository.findById(User.class, users.get(1).getId());
                User user2 = genericRepository.findById(User.class, users.get(2).getId());
                Post post = genericRepository.findById(Post.class,p.getId());
                PostVote postVote = new PostVote(user1,post,(short)+1);
                PostVote postVote1 = new PostVote(user2,post,(short)+1);
                genericRepository.insert(postVote);
                genericRepository.insert(postVote1);
            });
            em.clear();
            List<Integer> expectedIds = posts.stream().map(Post::getId).collect(Collectors.toList());
            List<Integer> resultIds = postRepository.getFinder().getMostVoted().getResults().stream().map(Post::getId).collect(Collectors.toList());


            Assertions.assertTrue(equalsUnorderedList(resultIds.subList(0,2), expectedIds.subList(5,7)));
            Assertions.assertTrue(equalsUnorderedList(resultIds.subList(2,7), expectedIds.subList(0,5)));
            Assertions.assertTrue(equalsUnorderedList(resultIds.subList(7,9), expectedIds.subList(7,9)));
        });
    }

    @ParameterizedTest
    @CsvSource({"1,2","5,3","0,8"})
    void paginationTest(int offset, int pageSize) throws Exception {
        doThenRollback(() -> {
            List<Integer> expected = postRepository.getFinder().getResults().stream().map(Post::getId).collect(Collectors.toList());
            List<Integer> result = postRepository.getFinder().offset(offset).limit(pageSize).getResults().stream().map(Post::getId).collect(Collectors.toList());
            Assertions.assertEquals(expected.subList(offset, pageSize + offset), result);
        });
    }
}
