package persistence.repo;

import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import persistence.model.Follow;
import persistence.model.Section;
import persistence.model.User;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;



@Classes(cdi = true, value = {SectionRepository.class, GenericRepository.class})
public class SectionRepositoryIT extends PersistenceIT{

    @FunctionalInterface
    private interface TriConsumer{
        void accept(int user, int section, Instant date);
    }

    @Inject private SectionRepository sectionRepository;
    @Inject private GenericRepository genericRepository;

    private List<Section> sections;

    @BeforeAll
    public void populate() throws Exception {
        doTransactional(() -> {
            sections = IntStream.range(1, 5).mapToObj(n -> {
                Section section = new Section();
                section.setName("section" + n);
                section.setDescription("description"+n);
                section.setBanner("banner"+n);
                section.setPicture("picture"+n);
                return section;
            }).map(genericRepository::insert).collect(Collectors.toList());
        });
    }

    @Test
    public void testRetrieve() throws Exception {
        doThenRollback(() -> {
            Section section = genericRepository.findByNaturalId(Section.class,sections.get(0).getName());
            Assertions.assertEquals(section.getId(),sections.get(0).getId());
            Assertions.assertEquals(section.getName(),sections.get(0).getName());
            Assertions.assertEquals(section.getBanner(),sections.get(0).getBanner());
            Assertions.assertEquals(section.getDescription(),sections.get(0).getDescription());
            Assertions.assertEquals(section.getPicture(),sections.get(0).getPicture());
        });

        doThenRollback(() -> {
            Section section = genericRepository.findByNaturalId(Section.class,sections.get(1).getName(),false);
            Assertions.assertEquals(section.getId(),sections.get(1).getId());
            Assertions.assertEquals(section.getName(),sections.get(1).getName());
            Assertions.assertEquals(section.getBanner(),sections.get(1).getBanner());
            Assertions.assertEquals(section.getDescription(),sections.get(1).getDescription());
            Assertions.assertEquals(section.getPicture(),sections.get(1).getPicture());
        });

        doThenRollback(() -> {
            Section section = genericRepository.findByNaturalId(Section.class,sections.get(2).getName(),true);
            Assertions.assertEquals(section.getId(),sections.get(2).getId());
            Assertions.assertEquals(section.getName(),sections.get(2).getName());
            Assertions.assertEquals(section.getBanner(),sections.get(2).getBanner());
            Assertions.assertEquals(section.getDescription(),sections.get(2).getDescription());
            Assertions.assertEquals(section.getPicture(),sections.get(2).getPicture());
        });

    }

    @Test
    void mostFollowedSections() throws Exception {
        doThenRollback((em) -> {
            List<User> users = IntStream.range(1, 10).mapToObj((n) -> {
                User user = new User();
                user.setEmail("email" + n + "@email.it");
                user.setUsername("username" + n);
                user.setPassword("password".getBytes(StandardCharsets.UTF_8));
                user.setSalt("salt".getBytes(StandardCharsets.UTF_8));
                user.setAdmin(false);
                user.setPicture("");
                return user;
            }).map(genericRepository::insert).collect(Collectors.toList());


            List<Section> sections = this.sections.stream().map(x -> genericRepository.findById(Section.class,x.getId())).collect(Collectors.toList());

            users.subList(0,2).forEach((u) -> genericRepository.insert(new Follow(u,sections.get(3))));
            users.subList(0,4).forEach((u) -> genericRepository.insert(new Follow(u,sections.get(1))));
            users.subList(0,7).forEach((u) -> genericRepository.insert(new Follow(u,sections.get(2))));
            users.forEach((u) -> genericRepository.insert(new Follow(u,sections.get(0))));

            List<Integer> expectedIds = IntStream.of(0,2,1,3).mapToObj(sections::get).map(Section::getId).collect(Collectors.toList());
            List<Integer> resultIds = sectionRepository.getMostFollowedSections().stream().map(Section::getId).collect(Collectors.toList()).subList(0,4);

            Assertions.assertEquals(expectedIds,resultIds);

        });
    }

    @Test
    void mostFollowedSectionsAfter() throws Exception {
        doThenRollback((em) -> {
            List<User> users = IntStream.range(1, 10).mapToObj((n) -> {
                User user = new User();
                user.setEmail("email" + n + "@email.it");
                user.setUsername("username" + n);
                user.setPassword("password".getBytes(StandardCharsets.UTF_8));
                user.setSalt("salt".getBytes(StandardCharsets.UTF_8));
                user.setAdmin(false);
                user.setPicture("");
                return user;
            }).map(genericRepository::insert).collect(Collectors.toList());


            Instant twoDaysAgo = Instant.now().minus(2, ChronoUnit.DAYS);
            Instant aMonthAgo = Instant.now().minus(30,ChronoUnit.DAYS);

            TriConsumer saveFollow = (user, section, date) ->
                    em.createNativeQuery("insert into follow(user_id, section_id, follow_date) values (?1,?2,?3)")
                    .setParameter(1,user)
                    .setParameter(2,section)
                    .setParameter(3,Date.from(date)).executeUpdate();

            users.stream().map(User::getId).forEach((id) -> saveFollow.accept(id, sections.get(0).getId(), twoDaysAgo));
            users.stream().map(User::getId).forEach((id) -> saveFollow.accept(id, sections.get(1).getId(), aMonthAgo));
            users.subList(0,4).stream().map(User::getId).forEach((id) -> saveFollow.accept(id, sections.get(2).getId(), twoDaysAgo));
            users.subList(4,9).stream().map(User::getId).forEach((id) -> saveFollow.accept(id, sections.get(2).getId(), aMonthAgo));

            em.clear();

            List<Integer> result = sectionRepository.getMostFollowedSections(twoDaysAgo).stream().map(Section::getId).collect(Collectors.toList()).subList(0,2);
            List<Integer> expected = IntStream.of(0,2).mapToObj(sections::get).map(Section::getId).collect(Collectors.toList());

            Assertions.assertEquals(result,expected);

            System.out.println(sectionRepository.getMostFollowedSections(twoDaysAgo).size());
            System.out.println(sections.size());
        });
    }


}
