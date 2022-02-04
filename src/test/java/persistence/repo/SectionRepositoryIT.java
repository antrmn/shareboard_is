package persistence.repo;

import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import persistence.model.Section;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Classes(cdi = true, value = {SectionRepository.class, GenericRepository.class})
public class SectionRepositoryIT extends PersistenceIT{

    @Inject private SectionRepository sectionRepository;
    @Inject private GenericRepository genericRepository;

    private List<Section> sections;

    @BeforeAll
    public void populate() throws Exception {
        doTransactional(() -> {
            sections = IntStream.range(1, 4).mapToObj(n -> {
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
            Section section = sectionRepository.getByName(sections.get(0).getName());
            Assertions.assertEquals(section.getId(),sections.get(0).getId());
            Assertions.assertEquals(section.getName(),sections.get(0).getName());
            Assertions.assertEquals(section.getBanner(),sections.get(0).getBanner());
            Assertions.assertEquals(section.getDescription(),sections.get(0).getDescription());
            Assertions.assertEquals(section.getPicture(),sections.get(0).getPicture());
        });

        doThenRollback(() -> {
            Section section = sectionRepository.getByName(sections.get(1).getName(),false);
            Assertions.assertEquals(section.getId(),sections.get(1).getId());
            Assertions.assertEquals(section.getName(),sections.get(1).getName());
            Assertions.assertEquals(section.getBanner(),sections.get(1).getBanner());
            Assertions.assertEquals(section.getDescription(),sections.get(1).getDescription());
            Assertions.assertEquals(section.getPicture(),sections.get(1).getPicture());
        });

        doThenRollback(() -> {
            Section section = sectionRepository.getByName(sections.get(2).getName(),true);
            Assertions.assertEquals(section.getId(),sections.get(2).getId());
            Assertions.assertEquals(section.getName(),sections.get(2).getName());
            Assertions.assertEquals(section.getBanner(),sections.get(2).getBanner());
            Assertions.assertEquals(section.getDescription(),sections.get(2).getDescription());
            Assertions.assertEquals(section.getPicture(),sections.get(2).getPicture());
        });

    }

}
