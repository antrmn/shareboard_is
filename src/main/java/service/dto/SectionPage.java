package service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class SectionPage {

    private String name;
    private String description;
    private String picture;
    private String banner;
}
