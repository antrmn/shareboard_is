package service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SectionPage {

    private Integer id;
    private String name;
    private String description;
    private String picture;
    private String banner;
    private Integer nFollowersTotal;
}
