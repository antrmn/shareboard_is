package service;

import persistence.model.Section;
import persistence.model.User;
import persistence.repo.SectionRepository;
import service.dto.SectionPage;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Produces;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
class TrendingSectionsProducer {
    @Inject SectionRepository sectionRepository;

    private List<SectionPage> topSections = new ArrayList<>();
    private List<SectionPage> tredingSections = new ArrayList<>();


    @Produces
    @RequestScoped
    @Named("topSections")
    public List<SectionPage> getTopSections(){
        return topSections;
    }

    @Produces
    @RequestScoped
    @Named("trendingSections")
    public List<SectionPage> getTrendingSections(){
        return tredingSections;
    }

    @Schedule(minute = "10")
    void loadTopSections(){
        topSections = sectionRepository.getMostFollowedSections().stream().map(this::map).collect(Collectors.toList());
    }

    @Schedule(minute="10")
    void loadTrendingSections(){
        tredingSections = sectionRepository.getMostFollowedSections(Instant.now().minus(7, ChronoUnit.DAYS))
                .stream().map(this::map).collect(Collectors.toList());
    }

    private SectionPage map(Section section){
        User user = null;
        return SectionPage.builder().id(section.getId())
                .name(section.getName())
                .picture(section.getPicture())
                .description(section.getDescription())
                .banner(section.getBanner())
                .nFollowers(section.getFollowCount())
                .isFollowed(false).build();
    }
}
